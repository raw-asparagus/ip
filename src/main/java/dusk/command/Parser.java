package dusk.command;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

/**
 * Parses user input strings and returns the corresponding command object.
 */
public class Parser {
    private static final Pattern FLAGS_PATTERN = Pattern.compile(
            "/(?<flag>on|from|to|by)\\s*(?<value>[^/]+)?",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern INPUT_PATTERN = Pattern.compile(
            "^(?<command>list|find|mark|unmark|delete|todo|deadline|event)"
                    + "(?:\\s+(?<description>[^/]+)(?<arguments>.*))?$",
            Pattern.CASE_INSENSITIVE
    );

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd")
                    .optionalStart()
                    .appendLiteral(' ')
                    .appendPattern("HHmm")
                    .optionalEnd()
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .toFormatter();

    /**
     * Parses a user input string and constructs the appropriate command object.
     *
     * @param duskIO  the console I/O
     * @param storage the storage object
     * @param tasks   the current task list
     * @param input   the raw user input string
     * @return the command object corresponding to the user input
     * @throws InputException if the command is invalid or incorrectly formatted
     */
    public static Command parse(DuskIO duskIO, Storage storage,
                                TaskList tasks, String input) throws InputException {
        if (input == null || input.trim().isEmpty()) {
            throw new InputException("Input cannot be empty");
        }

        Matcher matcher = INPUT_PATTERN.matcher(input.trim());
        if (!matcher.find()) {
            throw new InputException("Invalid command format");
        }

        String command = matcher.group("command").toLowerCase();
        String description = matcher.group("description");
        String arguments = matcher.group("arguments") != null ? matcher.group("arguments") : "";

        validateCommand(command, description, arguments);

        return switch (command) {
            case "list" -> parseListCommand(duskIO, tasks, arguments);
            case "find" -> new FindCommand(tasks, duskIO, description);
            case "mark" -> new MarkCommand(tasks, duskIO, storage, description, true);
            case "unmark" -> new MarkCommand(tasks, duskIO, storage, description, false);
            case "delete" -> new DeleteCommand(tasks, duskIO, storage, description);
            case "todo" -> new CreateTodoCommand(tasks, duskIO, storage, description);
            case "deadline" -> parseDeadlineCommand(duskIO, storage, tasks, description, arguments);
            case "event" -> parseEventCommand(duskIO, storage, tasks, description, arguments);
            default -> throw new InputException("Unknown command: " + command);
        };
    }

    private static void validateCommand(String command, String description,
                                        String arguments) throws InputException {
        switch (command) {
        case "list" -> {
            if (!arguments.isEmpty()) {
                validateDateTimeFlags(arguments);
            }
        }
        case "find" -> {
            if (description == null || description.trim().isEmpty()) {
                throw new InputException("Find command requires a search term");
            }
        }
        case "mark", "unmark", "delete" -> {
            if (description == null || description.trim().isEmpty()) {
                throw new InputException(command + " command requires a task number");
            }
            try {
                int taskNumber = Integer.parseInt(description.trim());
                if (taskNumber <= 0) {
                    throw new InputException(command +
                            " command requires a positive task number");
                }
            } catch (NumberFormatException e) {
                throw new InputException(command +
                        " command requires a valid integer task number");
            }
        }
        case "todo" -> {
            if (description == null || description.trim().isEmpty()) {
                throw new InputException("Todo command requires a description");
            }
            if (!arguments.isEmpty()) {
                throw new InputException("Todo command should not have any flags");
            }
        }
        case "deadline" -> {
            if (description == null || description.trim().isEmpty()) {
                throw new InputException("Deadline command requires a description");
            }
            if (!arguments.contains("/by")) {
                throw new InputException("Deadline command requires a /by datetime");
            }
            validateDateTimeFlags(arguments);
        }
        case "event" -> {
            if (description == null || description.trim().isEmpty()) {
                throw new InputException("Event command requires a description");
            }
            if (!arguments.contains("/from") || !arguments.contains("/to")) {
                throw new InputException(
                        "Event command requires both /from and /to datetimes");
            }
            validateDateTimeFlags(arguments);
        }
        default -> throw new InputException("Unknown command: " + command);
        }
    }

    private static void validateDateTimeFlags(String arguments) throws InputException {
        Matcher flagMatcher = FLAGS_PATTERN.matcher(arguments);
        while (flagMatcher.find()) {
            String flag = flagMatcher.group("flag");
            String value = flagMatcher.group("value");

            if (value == null || value.trim().isEmpty()) {
                throw new InputException("Missing datetime value for /" + flag + " flag");
            }

            try {
                parseDateTime(value.trim());
            } catch (InputException e) {
                throw new InputException("Invalid datetime format for /" + flag +
                        " flag. Use yyyy-MM-dd [HHmm]");
            }
        }
    }

    private static LocalDateTime parseDateTime(String dateTimeStr) throws InputException {
        try {
            return LocalDateTime.parse(dateTimeStr.trim(), DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InputException("Invalid datetime format. Use yyyy-MM-dd [HHmm]");
        }
    }

    private static Command parseListCommand(DuskIO duskIO, TaskList tasks,
                                            String arguments) throws InputException {
        if (arguments.isEmpty()) {
            return new ListCommand(tasks, duskIO, null, null, null);
        }

        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;
        LocalDateTime onDate = null;

        Matcher flagMatcher = FLAGS_PATTERN.matcher(arguments);
        while (flagMatcher.find()) {
            String flag = flagMatcher.group("flag").toLowerCase();
            String value = flagMatcher.group("value").trim();

            switch (flag) {
            case "from" -> fromDate = parseDateTime(value);
            case "to" -> toDate = parseDateTime(value);
            case "on" -> onDate = parseDateTime(value);
            default -> throw new InputException("Invalid flag for list command: /" + flag);
            }
        }

        if (onDate != null) {
            if (fromDate != null || toDate != null) {
                throw new InputException("Cannot use /on with /from or /to flags");
            }
            return new ListCommand(tasks, duskIO, onDate, null, null);
        }

        if (fromDate != null && toDate != null) {
            return new ListCommand(tasks, duskIO, null, fromDate, toDate);
        } else if (fromDate != null || toDate != null) {
            throw new InputException("Must specify both /from and /to dates");
        }

        return new ListCommand(tasks, duskIO, null, null, null);
    }

    private static Command parseDeadlineCommand(DuskIO duskIO, Storage storage,
                                                TaskList tasks, String description,
                                                String arguments) throws InputException {
        Matcher flagMatcher = FLAGS_PATTERN.matcher(arguments);
        LocalDateTime deadline = null;

        while (flagMatcher.find()) {
            if (flagMatcher.group("flag").equalsIgnoreCase("by")) {
                deadline = parseDateTime(flagMatcher.group("value").trim());
                break;
            }
        }

        if (deadline == null) {
            throw new InputException("Deadline command requires a /by datetime");
        }

        return new CreateDeadlineCommand(tasks, duskIO, storage, description, deadline);
    }

    private static Command parseEventCommand(DuskIO duskIO, Storage storage,
                                             TaskList tasks, String description,
                                             String arguments) throws InputException {
        Matcher flagMatcher = FLAGS_PATTERN.matcher(arguments);
        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        while (flagMatcher.find()) {
            String flag = flagMatcher.group("flag").toLowerCase();
            String value = flagMatcher.group("value").trim();

            switch (flag) {
            case "from" -> fromDate = parseDateTime(value);
            case "to" -> toDate = parseDateTime(value);
            default -> throw new InputException("Invalid flag for event command: /" + flag);
            }
        }

        if (fromDate == null || toDate == null) {
            throw new InputException("Event command requires both /from and /to datetimes");
        }

        if (toDate.isBefore(fromDate)) {
            throw new InputException("Event end time cannot be before start time");
        }

        return new CreateEventCommand(tasks, duskIO, storage, description, fromDate, toDate);
    }
}
