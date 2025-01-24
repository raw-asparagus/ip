import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dusk {
    // Commons
    private static final Logger logger = Logger.getLogger(Dusk.class.getName());

    // Commons to track tasks
    private static final int MAX_TASKS = 100;
    private static final Task[] tasks = new Task[MAX_TASKS];
    private static int taskCount = 0;

    public static void main(String[] args) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out))
        ) {
            printGreeting(writer);
            writer.flush();

            String input;
            while ((input = reader.readLine()) != null && !"bye".equalsIgnoreCase(input.trim())) {
                parseInput(input, writer);
                writer.flush();
            }

            printFarewell(writer);
            writer.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while handling I/O operations using BufferedReader/BufferedWriter.", e);
        }
    }

    private static void printGreeting(BufferedWriter writer) throws IOException {
        printLine(writer);
        writer.write("\t Hello! I'm Dusk\n\t Anything you want me to do for you? :D\n");
        printLine(writer);
    }

    private static void printFarewell(BufferedWriter writer) throws IOException {
        printLine(writer);
        writer.write("\t See ya! Hope to see you again soon! :3\n");
        printLine(writer);
    }

    private static void printLine(BufferedWriter writer) throws IOException {
        writer.write("\t____________________________________________________________\n");
    }

    private static void parseInput(String input, BufferedWriter writer) throws IOException {
        // list all tasks
        if (input.equalsIgnoreCase("list")) {
            listTasks(writer);
        }
        // mark task as done
        else if (input.toLowerCase().startsWith("mark ")) {
            String idx = input.substring("mark ".length());
            markTask(idx, writer, true);
        }
        // unmark task
        else if (input.toLowerCase().startsWith("unmark ")) {
            String idx = input.substring("unmark ".length());
            markTask(idx, writer, false);
        }
        // add to-do
        else if (input.toLowerCase().startsWith("todo ")) {
            String desc = input.substring("todo ".length());
            addTodo(desc, writer);
        }
        // add deadline
        else if (input.toLowerCase().startsWith("deadline ")) {
            String details = input.substring("deadline ".length());

            String desc = details;
            String by = "";
            int byIdx = details.toLowerCase().indexOf("/by ");
            if (byIdx != -1) {
                desc = details.substring(0, byIdx).trim();
                by = details.substring(byIdx + "/by ".length()).trim();
            }
            addDeadline(desc, by, writer);
        }
        // add event
        else if (input.toLowerCase().startsWith("event ")) {
            String details = input.substring("event ".length());

            String desc = details;
            String from = "";
            String to = "";
            int fromIdx = details.toLowerCase().indexOf("/from ");
            int toIdx = details.toLowerCase().indexOf("/to ");

            // Both /from and /to exist
            if (fromIdx != -1 && toIdx != -1) {
                if (fromIdx < toIdx) {
                    desc = details.substring(0, fromIdx).trim();
                    from = details.substring(fromIdx + "/from".length(), toIdx).trim();
                    to = details.substring(toIdx + "/to ".length()).trim();
                } else {
                    // /to is found before /from
                    desc = details.substring(0, toIdx).trim();
                    to = details.substring(toIdx + "/to ".length(), fromIdx).trim();
                    from = details.substring(fromIdx + "/from".length()).trim();
                }
            }
            // Only /from is present
            else if (fromIdx != -1) {
                desc = details.substring(0, fromIdx).trim();
                from = details.substring(fromIdx + "/from".length()).trim();
            }
            // Only /to is present
            else if (toIdx != -1) {
                desc = details.substring(0, toIdx).trim();
                to = details.substring(toIdx + "/to".length()).trim();
            }
            // Neither /from nor /to
            else {
                desc = details;
            }

            addEvent(desc, from, to, writer);
        } else {
            addTask(input, writer);
        }
    }

    private static void addTodo(String desc, BufferedWriter writer) throws IOException {
        printLine(writer);
        if (taskCount < MAX_TASKS) {
            Todo newTask = new Todo(desc);
            tasks[taskCount] = newTask;
            taskCount++;
            writer.write("\t Got it. I've added this task:\n\t   " + newTask + "\n\t Now you have " + taskCount + " tasks in the list.\n");
        } else {
            writer.write("\t Task list is full! Unable to add task!\n");
        }
        printLine(writer);
    }

    private static void addDeadline(String desc, String by, BufferedWriter writer) throws IOException {
        printLine(writer);
        if (taskCount < MAX_TASKS) {
            Deadline newTask = new Deadline(desc, by);
            tasks[taskCount] = newTask;
            taskCount++;
            writer.write("\t Got it. I've added this task:\n\t   " + newTask + "\n\t Now you have " + taskCount + " tasks in the list.\n");
        } else {
            writer.write("\t Task list is full! Unable to add task!\n");
        }
        printLine(writer);
    }

    private static void addEvent(String desc, String from, String to, BufferedWriter writer) throws IOException {
        printLine(writer);
        if (taskCount < MAX_TASKS) {
            Event newTask = new Event(desc, from, to);
            tasks[taskCount] = newTask;
            taskCount++;
            writer.write("\t Got it. I've added this task:\n\t   " + newTask + "\n\t Now you have " + taskCount + " tasks in the list.\n");
        } else {
            writer.write("\t Task list is full! Unable to add task!\n");
        }
        printLine(writer);
    }

    private static void addTask(String desc, BufferedWriter writer) throws IOException {
        printLine(writer);
        if (taskCount < MAX_TASKS) {
            Task newTask = new Task(desc);
            tasks[taskCount] = newTask;
            taskCount++;
            writer.write("\t added: " + newTask.getName() + "\n");
        } else {
            writer.write("\t Task list is full! Unable to add task!\n");
        }
        printLine(writer);
    }

    private static void markTask(String index, BufferedWriter writer, boolean isDone) throws IOException {
        try {
            int idx = Integer.parseInt(index) - 1;
            if (idx < 0 || idx >= taskCount) {
                printLine(writer);
                writer.write("\t Invalid task number!\n");
                printLine(writer);
                return;
            }

            Task task = tasks[idx];
            if (isDone) {
                task.markDone();
                printLine(writer);
                writer.write("\t Nice! I've marked this task as done:\n\t   " + task + "\n");
                printLine(writer);
            } else {
                task.markUndone();
                printLine(writer);
                writer.write("\t OK, I've marked this task as not done yet:\n\t   " + task + "\n");
                printLine(writer);
            }
        } catch (NumberFormatException e) {
            printLine(writer);
            writer.write("\t Please provide a valid task number to mark/unmark.\n");
            printLine(writer);
        }
    }

    private static void listTasks(BufferedWriter writer) throws IOException {
        printLine(writer);
        if (taskCount == 0) {
            writer.write("\t Task list is empty! Would you like to add some tasks?\n");
        } else {
            writer.write("\t Here are the tasks in your list:\n");
            for (int i = 0; i < taskCount; i++) {
                writer.write("\t " + (i + 1) + "." + tasks[i] + "\n");
            }
        }
        printLine(writer);
    }
}
