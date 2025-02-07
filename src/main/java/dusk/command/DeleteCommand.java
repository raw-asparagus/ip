package dusk.command;

import dusk.storage.Storage;
import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;

/**
 * Removes a task from the task list by its index and saves
 * the updated list to storage.
 */
public class DeleteCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;

    /**
     * Constructs a DeleteCommand.
     *
     * @param tasks       the task list from which a task will be removed
     * @param consoleIO   the console I/O for interaction
     * @param storage     the storage facility for saving task data
     * @param description the index of the task to be removed
     */
    public DeleteCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage, String description) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
    }

    /**
     * Removes a task by its index (parsed from the description),
     * displays a confirmation message, and saves the tasks asynchronously.
     *
     * @throws IOException       if an I/O error occurs while printing to the console
     * @throws InputException    if the index is invalid or not specified
     * @throws TaskListException if the task cannot be removed from the list
     */
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