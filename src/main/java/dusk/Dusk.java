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

    /**
     * Logger used throughout the Dusk application.
     */
    private static final Logger LOGGER = Logger.getLogger(Dusk.class.getName());

    /**
     * Provides task-related storage, including loading and saving tasks asynchronously.
     */
    private static final Storage STORAGE = new Storage();

    /**
     * Maintains the current list of tasks.
     */
    private static TaskList taskList;

    /**
     * Constructs a new Dusk instance and attempts to load tasks from storage.
     */
    public Dusk() {
        loadTasksFromStorage();
    }

    /**
     * Main entry point for the Dusk application.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        new Dusk();
        runApplication();
    }

    /**
     * Starts the Dusk application loop. This method displays greeting messages,
     * continues reading user input until "bye" is entered, and then prints a
     * farewell message before shutting down.
     */
    public static void runApplication() {
        try (ConsoleIO consoleIO = new ConsoleIO(System.in, System.out)) {
            consoleIO.print(GREETING_MESSAGES);
            String input;
            while ((input = consoleIO.readLine()) != null && !"bye".equalsIgnoreCase(input)) {
                processUserInput(consoleIO, input);
            }
            consoleIO.print(FAREWELL_MESSAGE);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while handling I/O operations using ConsoleIO.", e);
        } finally {
            STORAGE.shutdownExecutor();
        }
    }

    /**
     * Processes a single line of user input by parsing it into a command and executing it.
     *
     * @param consoleIO the console I/O handler
     * @param input the user input to process
     */
    private static void processUserInput(ConsoleIO consoleIO, String input) throws IOException {
        try {
            Command command = Parser.parse(consoleIO, STORAGE, taskList, input);
            command.execute();
        } catch (Exception e) {
            consoleIO.print(String.format("<!> %s", e.getMessage()));
        }
    }

    /**
     * Loads tasks asynchronously from STORAGE into a TaskList.
     * If the loading process results in an error, the exception is logged.
     */
    private void loadTasksFromStorage() {
        CompletableFuture<TaskList> loadFuture = STORAGE.loadTasksAsync();
        try {
            taskList = loadFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error loading tasks asynchronously.", e);
            Thread.currentThread().interrupt();
        } catch (CompletionException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return "Dusk heard: " + input;
    }
}
