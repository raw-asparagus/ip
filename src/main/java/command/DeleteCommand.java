package command;

import storage.Storage;
import task.Task;
import task.TaskList;
import task.TaskListException;
import ui.ConsoleIO;

import java.io.IOException;

public class DeleteCommand extends Command {
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
    public void execute() throws IOException, InputException, TaskListException {
        int idx;
        try {
            idx = Integer.parseInt(description) - 1;
        } catch (NumberFormatException e) {
            throw new InputException(
                    "Task number cannot be empty or invalid for a 'DELETE' command!"
            );
        }

        Task removedTask = tasks.removeTask(idx);
        consoleIO.print(
                "Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}
