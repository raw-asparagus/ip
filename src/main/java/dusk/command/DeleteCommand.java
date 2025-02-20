package dusk.command;

import dusk.storage.Storage;
import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.DuskIO;

import java.io.IOException;

/**
 * Deletes the task at the specified index from the task list.
 */
public class DeleteCommand extends Command {

    private final TaskList tasks;
    private final DuskIO duskIO;
    private final Storage storage;
    private final String description;

    /**
     * Constructs a command for deleting a task.
     *
     * @param tasks       the current task list
     * @param duskIO   the console I/O
     * @param storage     the storage object
     * @param description the description containing the index of the task to delete
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
            throw new InputException("Task number cannot be empty or invalid for a 'DELETE' command!");
        }

        Task removedTask = tasks.removeTask(taskIndex);
        duskIO.print(
                "Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, duskIO);
    }
}
