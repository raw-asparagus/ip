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
                input = input.trim();
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

    private static void parseInput(String input, BufferedWriter writer) throws java.io.IOException {
        if (input.equalsIgnoreCase("list")) {
            listTasks(writer);
        } else if (input.toLowerCase().startsWith("mark ")) {
            String idx = input.substring("mark ".length());
            markTask(idx, writer, true);
        } else if (input.toLowerCase().startsWith("unmark ")) {
            String idx = input.substring("unmark ".length());
            markTask(idx, writer, false);
        } else {
            addTask(input, writer);
        }
    }

    private static void addTask(String task, BufferedWriter writer) throws IOException {
        printLine(writer);
        if (taskCount < MAX_TASKS) {
            Task newTask = new Task(task);
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
            }

            Task task = tasks[idx];
            if (isDone) {
                task.markDone();
                printLine(writer);
                writer.write("\t Nice! I've marked this task as done:\n");
                writer.write("\t   " + task + "\n");
                printLine(writer);
            } else {
                task.markUndone();
                printLine(writer);
                writer.write("\t OK, I've marked this task as not done yet:\n");
                writer.write("\t   " + task + "\n");
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
            for (int i = 0; i < taskCount; i++) {
                writer.write("\t " + (i + 1) + "." + tasks[i] + "\n");
            }
        }
        printLine(writer);
    }
}
