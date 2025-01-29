package dusk.command;

import dusk.storage.Storage;
import dusk.task.MarkTaskException;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public abstract class Command {
    public abstract void execute() throws InputException, IOException, TaskListException, MarkTaskException;

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
