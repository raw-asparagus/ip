package command;

import storage.Storage;
import task.MarkTaskException;
import task.TaskList;
import task.TaskListException;
import ui.ConsoleIO;

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
