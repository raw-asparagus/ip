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

/**
 * The Dusk application is responsible for initializing and managing
 * user interactions, commands, and task storage.
 */
public class Dusk {

    /**
     * Common greeting messages displayed upon application launch.
     */
    public static final String[] GREETING_MESSAGES = {
            "Hello! I'm Dusk!",
            "Anything you want me to do for you? :D"
    };

    /**
     * Farewell message displayed when the user terminates the application.
     */
    public static final String FAREWELL_MESSAGE = "See ya! Hope to see you again soon! :3";

    private static final Logger logger = Logger.getLogger(Dusk.class.getName());
    private static final Storage storage = new Storage();
    private static TaskList taskList;

    /**
     * Constructs a new Dusk instance and attempts to load tasks.
     */
    public Dusk() {
        loadTasks();
    }

    /**
     * Starts the Dusk application loop. This method displays greeting messages,
     * continues reading user input until "bye" is entered, and then prints a
     * farewell message before shutting down.
     */
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

    /**
     * The main entry point for the Dusk application. Creates a Dusk instance
     * and invokes the runApp() method.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        new Dusk();
        runApp();
    }

    /**
     * Loads tasks asynchronously from storage into a TaskList object.
     * If the loading process results in an error, the exception is logged.
     */
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