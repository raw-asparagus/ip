package dusk.command;

import dusk.storage.Storage;
import dusk.task.MarkTaskException;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Represents an abstract command that can be executed.
 * All concrete commands should extend this class and implement
 * the {@link #execute()} method to carry out specific actions.
 */
public abstract class Command {

    /**
     * Executes the command's main actions.
     *
     * @throws InputException      if the user input is invalid for the command
     * @throws IOException         if an input or output error occurs
     * @throws TaskListException   if an error occurs when modifying a task list
     * @throws MarkTaskException   if an error occurs while marking a task
     */
    public abstract void execute() throws InputException, IOException, TaskListException, MarkTaskException;

    /**
     * Saves tasks asynchronously to storage, blocking until completion.
     *
     * @param storage  the {@link Storage} instance to handle task persistence
     * @param tasks    the {@link TaskList} to save
     * @param consoleIO the {@link ConsoleIO} used for printing any error messages
     */
    protected void saveAsync(Storage storage, TaskList tasks, ConsoleIO consoleIO) {
        CompletableFuture<Void> future = storage.saveTasksAsync(tasks)
                .exceptionally(ex -> {
                    try {
                        consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return null;
                });
        future.join();
    }
}