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
            "/(?<flag>on|from|to|by)\\s*(?<value>[^/]+)?",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern INPUT_PATTERN = Pattern.compile(
            "^(?<command>list|mark|unmark|delete|todo|deadline|event)"
                    + "(?:\\s+(?<description>[^/]+)(?<arguments>.*))?$",
            Pattern.CASE_INSENSITIVE
    );

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .optionalStart().appendLiteral(' ')
                    .appendPattern("HHmm").optionalEnd()
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .toFormatter();

    public static Command parse(ConsoleIO consoleIO, Storage storage,
                                TaskList tasks, String input) throws InputException {
        if (input.isBlank()) {
            throw new InputException("Input cannot be null or empty.");
        }

        Matcher inputMatcher = INPUT_PATTERN.matcher(input);
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

        String onStr = null;
        String fromStr = null;
        String toStr = null;
        String byStr = null;

        if (!arguments.isBlank()) {
            Matcher flagsMatcher = FLAGS_PATTERN.matcher(arguments);
            while (flagsMatcher.find()) {
                String foundFlag = flagsMatcher.group("flag");
                String foundValue = flagsMatcher.group("value");
                if (foundValue != null) {
                    foundValue = foundValue.trim();
                }
                if (foundFlag != null) {
                    switch (foundFlag.toLowerCase()) {
                        case "on" -> onStr = foundValue;
                        case "from" -> fromStr = foundValue;
                        case "to" -> toStr = foundValue;
                        case "by" -> byStr = foundValue;
                        default -> {
                        }
                    }
                }
            }
        }

        LocalDateTime onDateTime = parseDateTime(onStr);
        LocalDateTime fromDateTime = parseDateTime(fromStr);
        LocalDateTime toDateTime = parseDateTime(toStr);
        LocalDateTime byDateTime = parseDateTime(byStr);

        return switch (commandType) {
            case LIST -> new ListCommand(tasks, consoleIO, onDateTime, fromDateTime, toDateTime);
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
        if (dateTimeStr == null || dateTimeStr.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InputException("Use 'yyyy-MM-dd' or 'yyyy-MM-dd HHmm'. Invalid date/time format: "
                    + dateTimeStr);
        }
    }

}
