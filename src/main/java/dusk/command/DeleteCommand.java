package dusk.command;

import dusk.storage.Storage;
import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

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
                    "Task number cannot be empty or invalid for a 'DELETE' dusk.command!"
            );
        }

        Task removedTask = tasks.removeTask(idx);
        consoleIO.print(
                "Noted. I've removed this dusk.task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}
