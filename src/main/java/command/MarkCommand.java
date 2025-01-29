package command;

import storage.Storage;
import task.Task;
import task.TaskList;
import ui.ConsoleIO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MarkCommand implements Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final boolean isMark;

    public MarkCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage,
                       String description, boolean isMark) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.isMark = isMark;
    }

    @Override
    public void execute() throws IOException, DuskException, InputException {
        int idx;
        try {
            idx = Integer.parseInt(description) - 1;
        } catch (NumberFormatException e) {
            throw new InputException("task.Task number cannot be empty or invalid for a 'MARK'/'UNMARK' command!");
        }

        if (idx < 0 || idx >= tasks.size()) {
            throw new DuskException("Invalid task number: " + description);
        }

        Task task;
        try {
            task = tasks.getTask(idx);
        } catch (Exception e) {
            throw new DuskException(e.getMessage());
        }
        if (isMark) {
            if (task.getDone()) {
                consoleIO.print("task.Task already mark as done!");
            } else {
                task.markDone();
                consoleIO.print("Nice! I've marked this task as done:", "  " + task);

                // Async save, then wait for completion
                CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
                    try {
                        consoleIO.print("<!> Error saving tasks asynchronously: "
                                + ex.getMessage() + " at task " + description + " mark");
                    } catch (IOException e2) {
                        throw new RuntimeException(e2);
                    }
                    return null;
                });
                future.join();
            }
        } else {
            if (!task.getDone()) {
                consoleIO.print("task.Task already mark as not done!");
            } else {
                task.markUndone();
                consoleIO.print("OK! I've updated this task as not done:", "  " + task);

                // Async save, then wait for completion
                CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
                    try {
                        consoleIO.print("<!> Error saving tasks asynchronously: "
                                + ex.getMessage() + " at task " + description + " unmark");
                    } catch (IOException e2) {
                        throw new RuntimeException(e2);
                    }
                    return null;
                });
                future.join();
            }
        }
    }
}