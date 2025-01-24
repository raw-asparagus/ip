import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Dusk {
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
            e.printStackTrace();
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
        if ("list".equalsIgnoreCase(input.trim())) {
            listTasks(writer);
        } else {
            addTask(input, writer);
        }
    }

    private static void addTask(String task, BufferedWriter writer) throws IOException {
        printLine(writer);
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = new Task(task);
            taskCount++;
            writer.write("\t added: " + task + "\n");
        } else {
            writer.write("\t Task list is full! Unable to add task!\n");
        }
        printLine(writer);
    }

    private static void listTasks(BufferedWriter writer) throws IOException {
        printLine(writer);
        if (taskCount == 0) {
            writer.write("\t Task list is empty! Would you like to add some tasks?\n");
        } else {
            for (int i = 0; i < taskCount; i++) {
                writer.write("\t " + (i + 1) + ". " + tasks[i] + "\n");
            }
        }
        printLine(writer);
    }
}
