package dusk;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import dusk.command.Command;
import dusk.command.InputException;
import dusk.command.Parser;
import dusk.storage.Storage;
import dusk.storage.StorageException;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

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
    public Dusk() throws StorageException {
        loadTasksFromStorage();
    }

    public String getGreeting() {
        return String.join("\n", GREETING_MESSAGES);
    }

    /**
     * Loads tasks asynchronously from STORAGE into a TaskList.
     * If the loading process results in an error, the exception is logged.
     */
    private void loadTasksFromStorage() throws StorageException {
        CompletableFuture<TaskList> loadFuture = STORAGE.loadTasksAsync();
        try {
            taskList = loadFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.log(Level.SEVERE, "Error loading tasks asynchronously.", e);
            Thread.currentThread().interrupt();
            throw new StorageException("Failed to load tasks: " + e.getMessage());
        } catch (CompletionException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new StorageException("Failed to complete task loading: " + e.getMessage());
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
        StringWriter stringWriter = new StringWriter();
        try (DuskIO duskIO = new DuskIO(new StringReader(""), stringWriter)) {
            if (input == null || input.trim().isEmpty()) {
                throw new InputException("Please enter a command.");
            }
            Command command = Parser.parse(duskIO, STORAGE, taskList, input);
            command.execute();
            return stringWriter.toString();
        } catch (DuskException e) {
            // Enhanced error formatting with error type
            return String.format("❌ %s\n━━━━━━━━━━━━━━━━\n%s\n━━━━━━━━━━━━━━━━",
                    e.getErrorType().getLabel(),
                    e.getMessage());
        } catch (Exception e) {
            // Unexpected system errors
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            return String.format("⚠️ System Error\n━━━━━━━━━━━━━━━━\n%s\n━━━━━━━━━━━━━━━━",
                    e.getMessage());
        }
    }
}
