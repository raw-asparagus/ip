package dusk.command;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final Pattern FLAGS_PATTERN = Pattern.compile(
            "/(?<flag>by|from|to)\\s+(?<value>[^/]+)", Pattern.CASE_INSENSITIVE);

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .optionalStart().appendLiteral(' ')
                    .appendPattern("HHmm").optionalEnd()
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .toFormatter();



    public static Command parse(ConsoleIO consoleIO, Storage storage, TaskList tasks, String input)
            throws InputException {

        if (input.isEmpty()) {
            throw new InputException("Input cannot be null or empty.");
        }

        Pattern inputPattern = Pattern.compile(
                "^(?<command>list|mark|unmark|delete|todo|deadline|event)"
                        + "(?:\\s+(?<description>[^/]+)(?<arguments>.*))?$",
                Pattern.CASE_INSENSITIVE
        );
        Matcher inputMatcher = inputPattern.matcher(input);

        if (!inputMatcher.matches()) {
            throw new InputException("Invalid command: " + input);
        }

        CommandType commandType = CommandType.fromString(inputMatcher.group("command"));
        if (commandType == null) {
            throw new InputException("Unknown command: " + input);
        }

        String rawDescription = inputMatcher.group("description");
        String description = (rawDescription == null) ? "" : rawDescription.trim();

        String rawArguments = inputMatcher.group("arguments");
        String arguments = (rawArguments == null) ? "" : rawArguments.trim();

        String by = "";
        String from = "";
        String to = "";

        if (!arguments.isBlank()) {
            Matcher flagsMatcher = FLAGS_PATTERN.matcher(arguments);
            while (flagsMatcher.find()) {
                FlagType flag = FlagType.fromString(flagsMatcher.group("flag"));
                String value = flagsMatcher.group("value").trim();
                if (flag != null) {
                    switch (flag) {
                        case BY:
                            by = value;
                            break;
                        case FROM:
                            from = value;
                            break;
                        case TO:
                            to = value;
                            break;
                        default:
                    }
                }
            }
        }

        LocalDateTime byDateTime = parseDateTime(by);
        LocalDateTime fromDateTime = parseDateTime(from);
        LocalDateTime toDateTime = parseDateTime(to);

        return switch (commandType) {
            case LIST -> new ListCommand(tasks, consoleIO);
            case MARK -> new MarkCommand(tasks, consoleIO, storage, description, true);
            case UNMARK -> new MarkCommand(tasks, consoleIO, storage, description, false);
            case DELETE -> new DeleteCommand(tasks, consoleIO, storage, description);
            case TODO -> new CreateTodoCommand(tasks, consoleIO, storage, description);
            case DEADLINE -> new CreateDeadlineCommand(tasks, consoleIO, storage, description, byDateTime);
            case EVENT -> new CreateEventCommand(tasks, consoleIO, storage, description,
                    fromDateTime, toDateTime);
        };
    }

    private static LocalDateTime parseDateTime(String dateTimeStr) throws InputException {
        if (dateTimeStr.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InputException("Use 'yyyy-MM-dd' or 'yyyy-MM-dd HHmm'. Invalid date/time format: " + dateTimeStr);
        }
    }
}
