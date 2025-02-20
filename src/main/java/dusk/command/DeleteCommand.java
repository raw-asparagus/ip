package dusk.command;

import dusk.storage.Storage;
import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;

/**
 * Deletes the task at the specified index from the task list.
 */
public class DeleteCommand extends Command {

    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;

    /**
     * Constructs a command for deleting a task.
     *
     * @param tasks       the current task list
     * @param consoleIO   the console I/O
     * @param storage     the storage object
     * @param description the description containing the index of the task to delete
     */
    public DeleteCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage, String description) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
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
        consoleIO.print(
                "Noted. I've removed this task:",
                "  " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}
