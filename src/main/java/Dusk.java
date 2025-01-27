import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dusk {
    // Commons
    private static final Logger logger = Logger.getLogger(Dusk.class.getName());
    private static final List<Task> tasks = new ArrayList<>();

    // Commons messages
    public static final String[] GREETING = new String[] {
        "Hello! I'm Dusk",
        "Anything you want me to do for you? :D"
    };
    public static final String BYE = "See ya! Hope to see you again soon! :3";

    public enum CommandType {
        LIST,
        MARK,
        UNMARK,
        DELETE,
        TODO,
        DEADLINE,
        EVENT;

        public static CommandType fromString(String command) {
            try {
                return CommandType.valueOf(command.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public enum FlagType {
        BY,
        FROM,
        TO;

        public static FlagType fromString(String flag) {
            try {
                return FlagType.valueOf(flag.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))
        ) {
            printMessage(writer, GREETING);

            String input;
            while ((input = reader.readLine()) != null && !"bye".equalsIgnoreCase(input.trim())) {
                writer.write(input + "\n");
                writer.flush();
                try {
                    Command command = CommandParser.parse(writer, input, tasks);
                    command.execute();
                } catch (DuskException | InputException e) {
                    Dusk.printMessage(writer, "!! " + e.getMessage());
                }
            }

            printMessage(writer, BYE);
        } catch (IOException e) {
            logger.log(Level.SEVERE,
                    "An error occurred while handling I/O operations using BufferedReader/BufferedWriter.", e);
        }
    }

    private static void printMessage(BufferedWriter writer, String message) throws IOException {
        printLine(writer);
        writer.write("\t " + message + "\n");
        printLine(writer);
        writer.flush();
    }

    private static void printMessage(BufferedWriter writer, String[] messages) throws IOException {
        printLine(writer);
        for (String message : messages) {
            writer.write("\t " + message + "\n");
        }
        printLine(writer);
        writer.flush();
    }

    private static void printLine(BufferedWriter writer) throws IOException {
        writer.write("\t" + "_".repeat(60) + "\n");
    }

    public interface Command {
        void execute() throws DuskException, InputException, IOException;
    }

    public class CommandParser {
        public static Command parse(BufferedWriter writer, String input, List<Task> tasks) throws InputException {
            input = input.trim();
            if (input.isEmpty()) {
                throw new InputException("Input cannot be null or empty.");
            }

            Pattern inputPattern = Pattern.compile(
                    "^(?<command>list|mark|unmark|delete|todo|deadline|event)(?:\\s+(?<description>[^/]+)(?<arguments>.*))?$",
                    Pattern.CASE_INSENSITIVE);

            Matcher inputMatcher = inputPattern.matcher(input);
            if (!inputMatcher.matches()) {
                throw new InputException("Invalid command: " + input);
            }

            Dusk.CommandType commandType = CommandType.fromString(inputMatcher.group("command").trim());
            String rawDescription = inputMatcher.group("description");
            String rawArguments = inputMatcher.group("arguments");

            String description = (rawDescription == null) ? "" : rawDescription.trim();
            String arguments = (rawArguments == null) ? "" : rawArguments.trim();

            String by = "";
            String from = "";
            String to = "";
            if (!arguments.isBlank()) {
                Pattern flagsPattern = Pattern.compile("/(?<flag>by|from|to)\\s+(?<value>[^/]+)", Pattern.CASE_INSENSITIVE);
                Matcher flagsMatcher = flagsPattern.matcher(arguments);
                while (flagsMatcher.find()) {
                    Dusk.FlagType flag = Dusk.FlagType.fromString(flagsMatcher.group("flag").trim());
                    String value = flagsMatcher.group("value").trim();

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

            switch (commandType) {
                case LIST:
                    return new ListCommand(tasks, writer);
                case MARK:
                    return new MarkCommand(tasks, writer, description, true);
                case UNMARK:
                    return new MarkCommand(tasks, writer, description, false);
                case DELETE:
                    return new DeleteCommand(tasks, writer, description);
                case TODO:
                    return new CreateTodoCommand(tasks, writer, description);
                case DEADLINE:
                    return new CreateDeadlineCommand(tasks, writer, description, by);
                case EVENT:
                    return new CreateEventCommand(tasks, writer, description, from, to);
                default:
                    throw new InputException("Unknown command: " + commandType);
            }
        }
    }

    public static class ListCommand implements Command {
        private final List<Task> tasks;
        private final BufferedWriter writer;

        public ListCommand(List<Task> tasks, BufferedWriter writer) {
            this.tasks = tasks;
            this.writer = writer;
        }

        @Override
        public void execute() throws IOException {
            if (tasks.isEmpty()) {
                Dusk.printMessage(writer, "Task list is empty!");
            } else {
                String[] messages = new String[tasks.size() + 1];
                messages[0] = "Here are the tasks in your list:";
                for (int i = 1; i <= tasks.size(); i++) {
                    messages[i] = i + "." + tasks.get(i - 1);
                }
                Dusk.printMessage(writer, messages);
            }
        }
    }

    public static class MarkCommand implements Command {
        private final List<Task> tasks;
        private final BufferedWriter writer;
        private final String description;
        private final boolean isMark;

        public MarkCommand(List<Task> tasks, BufferedWriter writer, String description, boolean isMark) {
            this.tasks = tasks;
            this.writer = writer;
            this.description = description;
            this.isMark = isMark;
        }

        @Override
        public void execute() throws IOException, DuskException, InputException {
            int idx;
            try {
                idx = Integer.parseInt(description) - 1;
            } catch (NumberFormatException e) {
                throw new InputException("Task number cannot be empty or invalid for a 'MARK'/'UNMARK' command!");
            }
            if (idx < 0 || idx >= tasks.size()) {
                throw new DuskException("Invalid task number: " + description);
            }

            Task task = tasks.get(idx);
            if (isMark) {
                task.markDone();
                Dusk.printMessage(writer, new String[]{"Nice! I've marked this task as done:", "  " + task});
            } else {
                task.markUndone();
                Dusk.printMessage(writer, new String[]{"OK! I've updated this task as undone:", "  " + task});
            }
        }
    }

    public static class DeleteCommand implements Command {
        private final List<Task> tasks;
        private final BufferedWriter writer;
        private final String description;

        public DeleteCommand(List<Task> tasks, BufferedWriter writer, String description) {
            this.tasks = tasks;
            this.writer = writer;
            this.description = description;
        }

        @Override
        public void execute() throws IOException, DuskException, InputException {
            int idx;
            try {
                idx = Integer.parseInt(description) - 1;
            } catch (NumberFormatException e) {
                throw new InputException("Task number cannot be empty or invalid for a 'DELETE' command!");
            }
            if (idx < 0 || idx >= tasks.size()) {
                throw new DuskException("Invalid task number: " + description);
            }

            Task removedTask = tasks.remove(idx);
            Dusk.printMessage(writer, new String[] {
                    "Noted. I've removed this task:",
                    "  " + removedTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            });
        }
    }

    public static class CreateTodoCommand implements Command {
        private final List<Task> tasks;
        private final BufferedWriter writer;
        private final String description;

        public CreateTodoCommand(List<Task> tasks, BufferedWriter writer, String description) {
            this.tasks = tasks;
            this.writer = writer;
            this.description = description;
        }

        @Override
        public void execute() throws IOException, InputException {
            if (description.isEmpty()) {
                throw new InputException("A 'TODO' command must include a description.");
            }
            Todo newTask = new Todo(description);
            tasks.add(newTask);
            Dusk.printMessage(writer, new String[] {
                    "Got it. I've added this task:",
                    "  " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            });
        }
    }

    public static class CreateDeadlineCommand implements Command {
        private final List<Task> tasks;
        private final BufferedWriter writer;
        private final String description;
        private final String by;

        public CreateDeadlineCommand(List<Task> tasks, BufferedWriter writer, String description, String by) {
            this.tasks = tasks;
            this.writer = writer;
            this.description = description;
            this.by = by;
        }

        @Override
        public void execute() throws IOException, InputException {
            if (description.isEmpty()) {
                throw new InputException("A 'DEADLINE' command must include a description.");
            }
            Deadline newTask = new Deadline(description, by);
            tasks.add(newTask);
            Dusk.printMessage(writer, new String[] {
                    "Got it. I've added this task:",
                    "  " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            });
        }
    }

    public static class CreateEventCommand implements Command {
        private final List<Task> tasks;
        private final BufferedWriter writer;
        private final String description;
        private final String from;
        private final String to;

        public CreateEventCommand(List<Task> tasks, BufferedWriter writer, String description, String from, String to) {
            this.tasks = tasks;
            this.writer = writer;
            this.description = description;
            this.from = from;
            this.to = to;
        }

        @Override
        public void execute() throws IOException, InputException {
            if (description.isEmpty()) {
                throw new InputException("An 'EVENT' command must include a description.");
            }
            Event newTask = new Event(description, from, to);
            tasks.add(newTask);
            Dusk.printMessage(writer, new String[] {
                    "Got it. I've added this task:",
                    "  " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            });
        }
    }
}
