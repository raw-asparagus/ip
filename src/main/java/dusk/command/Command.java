package dusk.command;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import dusk.storage.Storage;
import dusk.task.MarkTaskException;
import dusk.task.TaskList;
import dusk.task.TaskListException;

/**
 * Represents an abstract command.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * @throws InputException    if there is an issue with user input
     * @throws IOException       if an I/O error occurs
     * @throws TaskListException if task list operations fail
     * @throws MarkTaskException if task marking fails
     */
    public abstract void execute() throws InputException, IOException, TaskListException, MarkTaskException;

    /**
     * Asynchronously saves tasks to storage and handles any exceptions.
     *
     * @param storage the storage used for saving tasks
     * @param tasks   the task list to save
     */
    protected void saveAsync(Storage storage, TaskList tasks) {
        CompletableFuture<Void> future = storage.saveTasksAsync(tasks)
                .exceptionally(exception -> {
                    throw new RuntimeException(exception);
                });
        future.join();
    }
}
