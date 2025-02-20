package dusk.command;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import dusk.storage.Storage;
import dusk.task.MarkTaskException;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.DuskIO;

/**
 * Represents an abstract command in the application.
 */
public abstract class Command {

    /**
     * Executes the command. Any exception arising from user input,
     * I/O operations, or task list operations will be thrown.
     *
     * @throws InputException    if there is an issue with user input
     * @throws IOException       if there is an I/O issue
     * @throws TaskListException if there is a task list operation issue
     * @throws MarkTaskException if there is a task marking/unmarking issue
     */
    public abstract void execute() throws InputException, IOException, TaskListException, MarkTaskException;

    /**
     * Asynchronously saves the tasks to storage and prints any error message to the console.
     *
     * @param storage the storage object handling file storage
     * @param tasks   the list of tasks
     * @param duskIO  the console I/O object for printing
     */
    protected void saveAsync(Storage storage, TaskList tasks, DuskIO duskIO) {
        CompletableFuture<Void> future = storage.saveTasksAsync(tasks)
                .exceptionally(exception -> {
                    try {
                        duskIO.print("<!> Error saving tasks asynchronously: " + exception.getMessage());
                    } catch (IOException ioException) {
                        throw new RuntimeException(ioException);
                    }
                    return null;
                });
        future.join();
    }
}
