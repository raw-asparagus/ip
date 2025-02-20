package dusk;

import dusk.command.Command;
import dusk.command.Parser;
import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

import java.io.StringReader;
import java.io.StringWriter;
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
     * Represents the delay in milliseconds before the application terminates its execution.
     */
    private static final long TERMINATION_DELAY_MS = 5000; // 5 seconds delay

    /**
     * Constructs a new Dusk instance and attempts to load tasks from storage.
     */
    public Dusk() {
        loadTasksFromStorage();
    }

    public String getGreeting() {
        return String.join("\n", GREETING_MESSAGES);
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
     * Generates a response for the user's chat message by parsing and executing the command.
     * Uses DuskIO as an intermediary to capture the output without writing to the terminal.
     *
     * @param input the user input to process
     * @return the response captured from command execution
     */
    public String getResponse(String input) {
        // Create a StringWriter-based DuskIO to capture output without writing to terminal
        StringWriter stringWriter = new StringWriter();
        try (DuskIO duskIO = new DuskIO(new StringReader(""), stringWriter)) {
            Command command = Parser.parse(duskIO, STORAGE, taskList, input);
            command.execute();
            return stringWriter.toString();
        } catch (Exception e) {
            return String.format("<!> %s", e.getMessage());
        }
    }
}
