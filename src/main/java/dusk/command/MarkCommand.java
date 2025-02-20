package dusk.command;

import dusk.storage.Storage;
import dusk.task.MarkTaskException;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;

/**
 * Marks or unmarks the task at the specified index from the task list.
 */
public class MarkCommand extends Command {

    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final boolean markAsDone;

    /**
     * Constructs a command for marking or unmarking a task as done.
     *
     * @param tasks       the current task list
     * @param consoleIO   the console I/O
     * @param storage     the storage object
     * @param description the index of the task to mark/unmark
     * @param markAsDone  whether to mark the task as done (true) or not (false)
     */
    public MarkCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage,
                       String description, boolean markAsDone) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.markAsDone = markAsDone;
    }

    @Override
    public void execute() throws TaskListException, InputException, MarkTaskException, IOException {
        int taskIndex;
        try {
            taskIndex = Integer.parseInt(description) - 1;
        } catch (NumberFormatException exception) {
            throw new InputException(
                    "Task number cannot be empty or invalid for a 'MARK'/'UNMARK' command!"
            );
        }

        if (markAsDone) {
            tasks.markTask(taskIndex);
            consoleIO.print(
                    "Nice! I've marked this task as done:",
                    "  " + tasks.getTask(taskIndex)
            );
        } else {
            tasks.unmarkTask(taskIndex);
            consoleIO.print(
                    "OK! I've updated this task to not done:",
                    "  " + tasks.getTask(taskIndex)
            );
        }

        saveAsync(storage, tasks, consoleIO);
    }
}
