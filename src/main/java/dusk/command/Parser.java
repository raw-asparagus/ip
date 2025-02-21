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

    // Pattern to match flags (on, from, to, by) and their corresponding values.
    private static final Pattern FLAGS_PATTERN = Pattern.compile(
            "/(?<flag>on|from|to|by)\\s*(?<value>[^/]+)?",
            Pattern.CASE_INSENSITIVE);

    // Pattern to match the complete user input: command with optional description and arguments.
    private static final Pattern INPUT_PATTERN = Pattern.compile(
            "^(?<command>list|find|mark|unmark|delete|todo|deadline|event)"
                    + "(?:\\s+(?<description>[^/]+)(?<arguments>.*))?$",
            Pattern.CASE_INSENSITIVE);

    // Formatter for parsing date and time strings.
    private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
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
    public static Command parse(final DuskIO duskIO, final Storage storage,
                                final TaskList tasks, final String input) throws InputException {
        final Matcher matcher = INPUT_PATTERN.matcher(input.trim());
        if (!matcher.matches()) {
            throw new InputException("Invalid command format!");
        }

        final String command = matcher.group("command").toLowerCase().trim();
        final String description = matcher.group("description") != null
                ? matcher.group("description").trim() : "";
        final String arguments = matcher.group("arguments") != null
                ? matcher.group("arguments").trim() : "";

        validateCommand(command, description, arguments);

        return switch (command) {
            case "list" -> parseListCommand(duskIO, tasks, arguments);
            case "deadline" -> parseDeadlineCommand(duskIO, storage, tasks, description, arguments);
            case "event" -> parseEventCommand(duskIO, storage, tasks, description, arguments);
            case "find" -> new FindCommand(tasks, duskIO, description);
            case "delete" -> new DeleteCommand(tasks, duskIO, storage, description);
            case "mark" -> new MarkCommand(tasks, duskIO, storage, description, true);
            case "unmark" -> new MarkCommand(tasks, duskIO, storage, description, false);
            case "todo" -> new CreateTodoCommand(tasks, duskIO, storage, description);
            default -> throw new InputException("Unknown command: " + command);
        };
    }

    /**
     * Validates the command and its arguments.
     *
     * @param command     the command string
     * @param description the command description
     * @param arguments   the command arguments
     * @throws InputException if validation fails
     */
    private static void validateCommand(final String command, final String description,
                                        final String arguments) throws InputException {
        if (command == null || command.isEmpty()) {
            throw new InputException("Command cannot be empty.");
        }
        // Commands that require a non-empty description.
        if ((command.equals("delete") || command.equals("mark") || command.equals("unmark") ||
                command.equals("find") || command.equals("todo") || command.equals("deadline") ||
                command.equals("event")) && description.isEmpty()) {
            throw new InputException("Missing description for command: " + command);
        }
        // For list command, validate any date/time flags.
        if (command.equals("list") && !arguments.isEmpty()) {
            validateDateTimeFlags(arguments);
        }
    }

    /**
     * Validates date/time flags contained in the arguments.
     *
     * @param arguments the raw arguments string
     * @throws InputException if any flag value is missing or invalid
     */
    private static void validateDateTimeFlags(final String arguments) throws InputException {
        final Matcher flagMatcher = FLAGS_PATTERN.matcher(arguments);
        while (flagMatcher.find()) {
            final String flag = flagMatcher.group("flag").toLowerCase();
            final String value = flagMatcher.group("value");
            if (value == null || value.trim().isEmpty()) {
                throw new InputException("Flag /" + flag + " must have a value.");
            }
        }
    }

    /**
     * Parses a date/time string into a LocalDateTime object.
     *
     * @param dateTimeStr the raw date/time string
     * @return the parsed LocalDateTime object
     * @throws InputException if the date/time format is invalid
     */
    private static LocalDateTime parseDateTime(final String dateTimeStr) throws InputException {
        try {
            return LocalDateTime.parse(dateTimeStr.trim(), DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InputException("Invalid date/time format: " + dateTimeStr);
        }
    }

    /**
     * Parses the input as a list command.
     *
     * @param duskIO    the console I/O
     * @param tasks     the current task list
     * @param arguments the arguments portion of the input
     * @return the ListCommand corresponding to the input
     * @throws InputException if the arguments are invalid
     */
    private static Command parseListCommand(final DuskIO duskIO, final TaskList tasks,
                                            final String arguments) throws InputException {
        LocalDateTime onDate = null;
        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        final Matcher flagMatcher = FLAGS_PATTERN.matcher(arguments);
        while (flagMatcher.find()) {
            final String flag = flagMatcher.group("flag").toLowerCase();
            final String value = flagMatcher.group("value").trim();
            switch (flag) {
            case "on":
                if (onDate != null) {
                    throw new InputException("Duplicate /on flag.");
                }
                onDate = parseDateTime(value);
                break;
            case "from":
                if (fromDate != null) {
                    throw new InputException("Duplicate /from flag.");
                }
                fromDate = parseDateTime(value);
                break;
            case "to":
                if (toDate != null) {
                    throw new InputException("Duplicate /to flag.");
                }
                toDate = parseDateTime(value);
                break;
            default:
                throw new InputException("Unknown flag: /" + flag);
            }
        }
        if ((fromDate != null && toDate == null) || (fromDate == null && toDate != null)) {
            throw new InputException("Both /from and /to must be specified together.");
        }
        return new ListCommand(tasks, duskIO, onDate, fromDate, toDate);
    }

    /**
     * Parses the input as a deadline command.
     *
     * @param duskIO      the console I/O
     * @param storage     the storage object
     * @param tasks       the current task list
     * @param description the command description
     * @param arguments   the additional arguments
     * @return the DeadlineCommand corresponding to the input
     * @throws InputException if the arguments are invalid
     */
    private static Command parseDeadlineCommand(final DuskIO duskIO, final Storage storage,
                                                final TaskList tasks, final String description,
                                                final String arguments) throws InputException {
        final Matcher flagMatcher = FLAGS_PATTERN.matcher(arguments);
        String byValue = null;
        while (flagMatcher.find()) {
            final String flag = flagMatcher.group("flag").toLowerCase();
            if ("by".equals(flag)) {
                if (byValue != null) {
                    throw new InputException("Duplicate /by flag.");
                }
                byValue = flagMatcher.group("value").trim();
            } else {
                throw new InputException("Unexpected flag /" + flag + " in deadline command.");
            }
        }
        if (byValue == null || byValue.isEmpty()) {
            throw new InputException("Deadline command requires a /by flag with a valid date/time.");
        }
        LocalDateTime byDateTime = parseDateTime(byValue);
        return new CreateDeadlineCommand(tasks, duskIO, storage, description, byDateTime);
    }

    /**
     * Parses the input as an event command.
     *
     * @param duskIO      the console I/O
     * @param storage     the storage object
     * @param tasks       the current task list
     * @param description the command description
     * @param arguments   the additional arguments
     * @return the EventCommand corresponding to the input
     * @throws InputException if the arguments are invalid
     */
    private static Command parseEventCommand(final DuskIO duskIO, final Storage storage,
                                             final TaskList tasks, final String description,
                                             final String arguments) throws InputException {
        LocalDateTime fromDate = null;
        LocalDateTime toDate = null;

        final Matcher flagMatcher = FLAGS_PATTERN.matcher(arguments);
        while (flagMatcher.find()) {
            final String flag = flagMatcher.group("flag").toLowerCase();
            final String value = flagMatcher.group("value").trim();
            switch (flag) {
            case "from":
                if (fromDate != null) {
                    throw new InputException("Duplicate /from flag.");
                }
                fromDate = parseDateTime(value);
                break;
            case "to":
                if (toDate != null) {
                    throw new InputException("Duplicate /to flag.");
                }
                toDate = parseDateTime(value);
                break;
            default:
                throw new InputException("Unexpected flag /" + flag + " in event command.");
            }
        }
        if (fromDate == null || toDate == null) {
            throw new InputException("Event command requires both /from and /to flags with valid date/time values.");
        }
        return new CreateEventCommand(tasks, duskIO, storage, description, fromDate, toDate);
    }
}
