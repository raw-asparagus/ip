package dusk.command;

import java.io.IOException;

import dusk.storage.Storage;
import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.DuskIO;

/**
 * Command for deleting a specified task.
 */
public class DeleteCommand extends Command {

    private final TaskList tasks;
    private final DuskIO duskIO;
    private final Storage storage;
    private final String description;

    /**
     * Constructs a DeleteCommand.
     *
     * @param tasks       the current task list
     * @param duskIO      the I/O interface
     * @param storage     the storage handler
     * @param description the command description containing the task index to delete
     */
    public DeleteCommand(TaskList tasks, DuskIO duskIO, Storage storage, String description) {
        this.tasks = tasks;
        this.duskIO = duskIO;
        this.storage = storage;
        this.description = description;
    }

    @Override
    public void execute() throws IOException, InputException, TaskListException {
        int taskIndex;
        try {
            taskIndex = Integer.parseInt(description) - 1;
        } catch (NumberFormatException exception) {
            throw new InputException("Task number cannot be empty or invalid for a delete command!");
        }

        Task removedTask = tasks.removeTask(taskIndex);
        duskIO.print(
                "Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );
        saveAsync(storage, tasks);
    }
}
