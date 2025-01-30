package dusk;

import dusk.command.Command;
import dusk.command.Parser;
import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dusk {
    // Common messages
    public static final String[] GREETING_MESSAGES = {
            "Hello! I'm Dusk",
            "Anything you want me to do for you? :D"
    };
    public static final String FAREWELL_MESSAGE = "See ya! Hope to see you again soon! :3";

    private static final Logger logger = Logger.getLogger(Dusk.class.getName());
    private static final Storage storage = new Storage();
    private static TaskList taskList;

    // Constructor
    public Dusk() {
        loadTasks();
    }

    public static void runApp() {
        try (ConsoleIO consoleIO = new ConsoleIO(System.in, System.out)) {
            consoleIO.print(GREETING_MESSAGES);
            String input;
            while ((input = consoleIO.readLine()) != null && !"bye".equalsIgnoreCase(input)) {
                consoleIO.debugPrint(input);
                try {
                    Command command = Parser.parse(consoleIO, storage, taskList, input);
                    command.execute();
                } catch (Exception e) {
                    consoleIO.print("<!> " + e.getMessage());
                }
            }
            consoleIO.print(FAREWELL_MESSAGE);
        } catch (IOException e) {
            logger.log(
                    Level.SEVERE,
                    "An error occurred while handling I/O operations using ConsoleIO.",
                    e
            );
        } finally {
            storage.shutdownExecutor();
        }
    }

    public static void main(String[] args) {
        new Dusk();
        runApp();
    }

    private void loadTasks() {
        CompletableFuture<TaskList> loadFuture = storage.loadTasksAsync();
        try {
            taskList = loadFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Error loading tasks asynchronously.", e);
        } catch (CompletionException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
