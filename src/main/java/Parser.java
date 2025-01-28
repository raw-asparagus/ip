import storage.Storage;
import task.TaskList;
import ui.ConsoleIO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static Command parse(ConsoleIO consoleIO, Storage storage, TaskList tasks, String input)
            throws InputException {
        if (input.isEmpty()) {
            throw new InputException("Input cannot be null or empty.");
        }

        Pattern inputPattern = Pattern.compile(
                "^(?<command>list|mark|unmark|delete|todo|deadline|event)(?:\\s+(?<description>[^/]+)(?<arguments>.*))?$",
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
            Pattern flagsPattern = Pattern.compile("/(?<flag>by|from|to)\\s+(?<value>[^/]+)", Pattern.CASE_INSENSITIVE);
            Matcher flagsMatcher = flagsPattern.matcher(arguments);
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
                            break;
                    }
                }
            }
        }

        return switch (commandType) {
            case LIST -> new ListCommand(tasks, consoleIO);
            case MARK -> new MarkCommand(tasks, consoleIO, storage, description, true);
            case UNMARK -> new MarkCommand(tasks, consoleIO, storage, description, false);
            case DELETE -> new DeleteCommand(tasks, consoleIO, storage, description);
            case TODO -> new CreateTodoCommand(tasks, consoleIO, storage, description);
            case DEADLINE -> new CreateDeadlineCommand(tasks, consoleIO, storage, description, by);
            case EVENT -> new CreateEventCommand(tasks, consoleIO, storage, description, from, to);
        };
    }
}