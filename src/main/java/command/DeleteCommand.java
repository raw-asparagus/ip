package command;

import storage.Storage;
import task.Task;
import task.TaskList;
import ui.ConsoleIO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class DeleteCommand implements Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;

    public DeleteCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage, String description) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
    }

    @Override
    public void execute() throws IOException, DuskException, InputException {
        int idx;
        try {
            idx = Integer.parseInt(description) - 1;
        } catch (NumberFormatException e) {
            throw new InputException("task.Task number cannot be empty or invalid for a 'DELETE' command!");
        }

        if (idx < 0 || idx >= tasks.size()) {
            throw new DuskException("Invalid task number: " + description);
        }

        Task removedTask = tasks.removeTask(idx);
        consoleIO.print(
                "Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        // Async save, then wait for completion
        CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
            try {
                consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage()
                        + " at task " + description + " deletion");
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
            return null;
        });
        future.join();
    }
}