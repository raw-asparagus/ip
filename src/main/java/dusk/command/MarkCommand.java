package dusk.command;

import dusk.storage.Storage;
import dusk.task.MarkTaskException;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;

/**
 * Marks a task as done or not done based on the user's request,
 * and saves the updated task list.
 */
public class MarkCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final boolean isMark;

    /**
     * Constructs a MarkCommand.
     *
     * @param tasks       the task list containing tasks to mark or unmark
     * @param consoleIO   the console I/O for interaction
     * @param storage     the storage facility for saving task data
     * @param description the index of the task to be marked/unmarked
     * @param isMark      {@code true} if marking as done, {@code false} if unmarking
     */
    public MarkCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage,
                       String description, boolean isMark) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.isMark = isMark;
    }

    /**
     * Marks or unmarks the specified task, displays a confirmation message,
     * and saves the tasks asynchronously.
     *
     * @throws TaskListException if the task index is invalid
     * @throws InputException    if the description does not specify a valid index
     * @throws MarkTaskException if an error occurs while marking the task
     * @throws IOException       if an I/O error occurs while printing to the console
     */
    @Override
    public void execute() throws TaskListException, InputException, MarkTaskException, IOException {
        int idx;
        try {
            idx = Integer.parseInt(description) - 1;
        } catch (NumberFormatException e) {
            throw new InputException(
                    "Task number cannot be empty or invalid for a 'MARK'/'UNMARK' dusk.command!"
            );
        }

        if (isMark) {
            tasks.markTask(idx);
            consoleIO.print("Nice! I've marked this dusk.task as done:", "  " + tasks.getTask(idx));
        } else {
            tasks.unmarkTask(idx);
            consoleIO.print("OK! I've updated this dusk.task to not done:", "  " + tasks.getTask(idx));
        }

        saveAsync(storage, tasks, consoleIO);
    }
}