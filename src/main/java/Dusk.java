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
    public static final String[] GREETING = new String[] { "Hello! I'm Dusk",
            "Anything you want me to do for you? :D" };
    public static final String BYE = "See ya! Hope to see you again soon! :3";

    public static void main(String[] args) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))) {
            printMessage(writer, GREETING);

            String input;
            while ((input = reader.readLine()) != null && !"bye".equalsIgnoreCase(input.trim())) {
                try {
                    parseInput(writer, input);
                } catch (DuskException | InputException e) {
                    printMessage(writer, "!! " + e.getMessage());
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

    private static void parseInput(BufferedWriter writer, String input)
            throws IOException, DuskException, InputException {
        input = input.trim();
        if (input.isEmpty()) {
            throw new InputException("Input cannot be null or empty.");
        }

        Pattern inputPattern = Pattern.compile(
                "^(?<command>list|mark|delete|todo|deadline|event)(?:\\s+(?<description>[^/]+)(?<arguments>.*))?$",
                Pattern.CASE_INSENSITIVE);

        Matcher inputMatcher = inputPattern.matcher(input);
        if (!inputMatcher.matches()) {
            throw new InputException("Invalid command: " + input);
        }

        String command = inputMatcher.group("command").trim();
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
                String flag = flagsMatcher.group("flag").toLowerCase().trim();
                String value = flagsMatcher.group("value").trim();

                switch (flag) {
                    case "by":
                        by = value;
                        break;
                    case "from":
                        from = value;
                        break;
                    case "to":
                        to = value;
                        break;
                    default:
                        break;
                }
            }
        }

        int idx = -1;
        switch (command) {
            case "mark":
            case "unmark":
            case "delete":
                try {
                    idx = Integer.parseInt(description) - 1;
                } catch (NumberFormatException e) {
                    throw new InputException("Task number cannot be empty for a '" + command + "' command!");
                }
                if (idx <= 0 || idx > tasks.size()) {
                    throw new DuskException("Invalid task number: " + description);
                }
                break;
            case "todo":
            case "deadline":
            case "event":
                if (description.isEmpty()) {
                    throw new InputException("A '" + command + "' command must include a description.");
                }
                break;
            default:
                break;
        }

        switch (command) {
            case "list":
                listTasks(writer);
                break;
            case "mark":
            case "unmark":
                markTask(writer, idx, command.equalsIgnoreCase("mark"));
                break;
            case "delete":
                deleteTask(writer, idx);
            case "todo":
                addTodo(writer, description);
                break;
            case "deadline":
                addDeadline(writer, description, by);
                break;
            case "event":
                addEvent(writer, description, from, to);
                break;
            default:
                break;
        }
    }

    private static void addTodo(BufferedWriter writer, String description) throws IOException {
        Todo newTask = new Todo(description);
        tasks.add(newTask);
        printMessage(writer, new String[] {
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        });
    }

    private static void addDeadline(BufferedWriter writer, String description, String by) throws IOException {
        Deadline newTask = new Deadline(description, by);
        tasks.add(newTask);
        printMessage(writer, new String[] {
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        });
    }

    private static void addEvent(BufferedWriter writer, String description, String from, String to) throws IOException {
        Event newTask = new Event(description, from, to);
        tasks.add(newTask);
        printMessage(writer, new String[] {
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        });
    }

    private static void markTask(BufferedWriter writer, int idx, boolean isDone) throws IOException, DuskException {
        try {
            Task task = tasks.get(idx);
            if (isDone) {
                task.markDone();
                printMessage(writer, new String[]{"Nice! I've marked this task as done:", "  " + task});
            } else {
                task.markUndone();
                printMessage(writer, new String[]{"Nice! I've marked this task as done:", "  " + task});
            }
        } catch (IndexOutOfBoundsException e) {
            throw new DuskException("Invalid task number: " + idx);
        }
    }

    private static void deleteTask(BufferedWriter writer, int idx) throws IOException, DuskException {
        try {
            Task removedTask = tasks.remove(idx);
            printMessage(writer, new String[] {
                    "Noted. I've removed this task:",
                    "  " + removedTask,
                    "Now you have " + tasks.size() + " tasks in the list."
            });
        } catch (IndexOutOfBoundsException e) {
            throw new DuskException("Invalid task number: " + idx);
        }
    }

    private static void listTasks(BufferedWriter writer) throws IOException {
        if (tasks.isEmpty()) {
            printMessage(writer, "Task list is empty!");
        } else {
            String[] messages = new String[tasks.size() + 1];
            messages[0] = "Here are the tasks in your list:";
            for (int i = 1; i <= tasks.size(); i++) {
                messages[i] = i + "." + tasks.get(i - 1);
            }
            printMessage(writer, messages);
        }
    }
}
