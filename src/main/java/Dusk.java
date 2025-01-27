import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dusk {
    // Commons messages
    public static final String[] GREETING = {
            "Hello! I'm Dusk",
            "Anything you want me to do for you? :D"
    };
    public static final String BYE = "See ya! Hope to see you again soon! :3";

    // Commons
    private static final Logger logger = Logger.getLogger(Dusk.class.getName());
    private static final Storage storage = new Storage();
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        CompletableFuture<List<Task>> loadFuture = storage.loadTasksAsync();
        try {
            List<Task> loadedTasks = loadFuture.get();
            tasks.addAll(loadedTasks);
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Error loading tasks asynchronously.", e);
        }

        try (ConsoleIO consoleIO = new ConsoleIO(System.in, System.out)) {
            consoleIO.print(GREETING);

            String input;
            while ((input = consoleIO.readLine()) != null && !"bye".equalsIgnoreCase(input)) {
                // Echo user input for debugging
                consoleIO.getWriter().write(input + "\n");
                consoleIO.getWriter().flush();

                try {
                    Command command = Parser.parse(consoleIO, input, tasks);
                    command.execute();
                } catch (DuskException | InputException e) {
                    consoleIO.print("<!> " + e.getMessage());
                }
            }

            consoleIO.print(BYE);
        } catch (IOException e) {
            logger.log(
                    Level.SEVERE,
                    "An error occurred while handling I/O operations using ConsoleIO.",
                    e
            );
        } finally {
            // Cleanly shut down the executor to end the application
            storage.shutdownExecutor();
        }
    }

    public interface Command {
        void execute() throws DuskException, InputException, IOException;
    }

    public static class Parser {
        public static Command parse(ConsoleIO consoleIO, String input, List<Task> tasks)
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
                case MARK -> new MarkCommand(tasks, consoleIO, description, true);
                case UNMARK -> new MarkCommand(tasks, consoleIO, description, false);
                case DELETE -> new DeleteCommand(tasks, consoleIO, description);
                case TODO -> new CreateTodoCommand(tasks, consoleIO, description);
                case DEADLINE -> new CreateDeadlineCommand(tasks, consoleIO, description, by);
                case EVENT -> new CreateEventCommand(tasks, consoleIO, description, from, to);
            };
        }
    }

    public static class ListCommand implements Command {
        private final List<Task> tasks;
        private final ConsoleIO consoleIO;

        public ListCommand(List<Task> tasks, ConsoleIO consoleIO) {
            this.tasks = tasks;
            this.consoleIO = consoleIO;
        }

        @Override
        public void execute() throws IOException {
            if (tasks.isEmpty()) {
                consoleIO.print("Task list is empty!");
            } else {
                String[] messages = new String[tasks.size() + 1];
                messages[0] = "Here are the tasks in your list:";
                for (int i = 1; i <= tasks.size(); i++) {
                    messages[i] = i + "." + tasks.get(i - 1);
                }
                consoleIO.print(messages);
            }
        }
    }

    public static class MarkCommand implements Command {
        private final List<Task> tasks;
        private final ConsoleIO consoleIO;
        private final String description;
        private final boolean isMark;

        public MarkCommand(List<Task> tasks, ConsoleIO consoleIO, String description, boolean isMark) {
            this.tasks = tasks;
            this.consoleIO = consoleIO;
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
                if (task.getDone()) {
                    consoleIO.print("Task already mark as done!");
                } else {
                    task.markDone();
                    consoleIO.print("Nice! I've marked this task as done:", "  " + task);

                    // Async save, then wait for completion
                    CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
                        try {
                            consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage() + " at task " + description + " mark");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    });
                    future.join();
                }
            } else {
                if (!task.getDone()) {
                    consoleIO.print("Task already mark as not done!");
                } else {
                    task.markUndone();
                    consoleIO.print("OK! I've updated this task as not done:", "  " + task);

                    // Async save, then wait for completion
                    CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
                        try {
                            consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage() + " at task " + description + " unmark");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return null;
                    });
                    future.join();
                }
            }
        }
    }

    public static class DeleteCommand implements Command {
        private final List<Task> tasks;
        private final ConsoleIO consoleIO;
        private final String description;

        public DeleteCommand(List<Task> tasks, ConsoleIO consoleIO, String description) {
            this.tasks = tasks;
            this.consoleIO = consoleIO;
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
            consoleIO.print(
                    "Noted. I've removed this task:",
                    "  " + removedTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            );

            // Async save, then wait for completion
            CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
                try {
                    consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage() + " at task " + description + " deletion");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
            future.join();
        }
    }

    public static class CreateTodoCommand implements Command {
        private final List<Task> tasks;
        private final ConsoleIO consoleIO;
        private final String description;

        public CreateTodoCommand(List<Task> tasks, ConsoleIO consoleIO, String description) {
            this.tasks = tasks;
            this.consoleIO = consoleIO;
            this.description = description;
        }

        @Override
        public void execute() throws IOException, InputException {
            if (description.isEmpty()) {
                throw new InputException("A 'TODO' command must include a description.");
            }
            Todo newTask = new Todo(description);
            tasks.add(newTask);
            consoleIO.print(
                    "Got it. I've added this task:",
                    "  " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            );

            // Async save, then wait for completion
            CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
                try {
                    consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage() + " at task " + newTask);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
            future.join();
        }
    }

    public static class CreateDeadlineCommand implements Command {
        private final List<Task> tasks;
        private final ConsoleIO consoleIO;
        private final String description;
        private final String by;

        public CreateDeadlineCommand(List<Task> tasks, ConsoleIO consoleIO, String description, String by) {
            this.tasks = tasks;
            this.consoleIO = consoleIO;
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
            consoleIO.print(
                    "Got it. I've added this task:",
                    "  " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            );

            // Async save, then wait for completion
            CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
                try {
                    consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage() + " at task " + newTask);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
            future.join();
        }
    }

    public static class CreateEventCommand implements Command {
        private final List<Task> tasks;
        private final ConsoleIO consoleIO;
        private final String description;
        private final String from;
        private final String to;

        public CreateEventCommand(List<Task> tasks, ConsoleIO consoleIO, String description, String from, String to) {
            this.tasks = tasks;
            this.consoleIO = consoleIO;
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
            consoleIO.print(
                    "Got it. I've added this task:",
                    "  " + newTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            );

            // Async save, then wait for completion
            CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
                try {
                    consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage() + " at task " + newTask);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return null;
            });
            future.join();
        }
    }
}
