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
import dusk.ui.DuskResponse;

/**
 * The main application class for Dusk. This class is responsible for initializing tasks,
 * managing user commands, and handling responses.
 */
public class Dusk {

    /** Greeting messages displayed upon application launch. */
    public static final String[] GREETING_MESSAGES = {
            "Hello! I'm Dusk!",
            "Anything you want me to do for you? :D"
    };

    /** Farewell message displayed when the application terminates. */
    public static final String FAREWELL_MESSAGE = "See ya! Hope to see you again soon! :3";

    private static final Logger LOGGER = Logger.getLogger(Dusk.class.getName());
    private static final Storage STORAGE = new Storage();
    private static TaskList taskList;

    /**
     * Constructs a new Dusk instance and loads the task list from storage.
     *
     * @throws StorageException if an error occurs during task loading.
     */
    public Dusk() throws StorageException {
        loadTasksFromStorage();
    }

    /**
     * Returns the greeting message.
     *
     * @return the greeting message.
     */
    public String getGreeting() {
        return new DuskResponse(String.join("\n", GREETING_MESSAGES),
                DuskResponse.ResponseType.NORMAL).getMessage();
    }

    /**
     * Loads tasks asynchronously from storage.
     *
     * @throws StorageException if there is an error loading tasks.
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
     * Processes the user input and returns the corresponding response.
     *
     * @param input the user's input command.
     * @return a DuskResponse based on the command execution.
     */
    public DuskResponse getResponse(String input) {
        StringWriter stringWriter = new StringWriter();
        try (DuskIO duskIO = new DuskIO(new StringReader(""), stringWriter)) {
            if (input == null || input.trim().isEmpty()) {
                throw new InputException("Please enter a command.");
            }
            Command command = Parser.parse(duskIO, STORAGE, taskList, input);
            command.execute();
            return new DuskResponse(stringWriter.toString(), DuskResponse.ResponseType.NORMAL);
        } catch (DuskException e) {
            return new DuskResponse(
                    String.format("❌\t%s:\n\t%s", e.getErrorType().getLabel(), e.getMessage()),
                    DuskResponse.ResponseType.ERROR
            );
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error", e);
            return new DuskResponse(
                    String.format("⚠️\tSystem Error:\n\t%s", e.getMessage()),
                    DuskResponse.ResponseType.SYSTEM_ERROR
            );
        }
    }
}
