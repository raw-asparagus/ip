package command;

import storage.Storage;
import task.MarkTaskException;
import task.TaskList;
import task.TaskListException;
import ui.ConsoleIO;

import java.io.IOException;

public class MarkCommand extends Command {
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
    public void execute() throws TaskListException, InputException, MarkTaskException, IOException {
        int idx;
        try {
            idx = Integer.parseInt(description) - 1;
        } catch (NumberFormatException e) {
            throw new InputException(
                    "Task number cannot be empty or invalid for a 'MARK'/'UNMARK' command!"
            );
        }

        if (isMark) {
            tasks.markTask(idx);
            consoleIO.print("Nice! I've marked this task as done:", "  " + tasks.getTask(idx));
        } else {
            tasks.unmarkTask(idx);
            consoleIO.print("OK! I've updated this task to not done:", "  " + tasks.getTask(idx));
        }

        saveAsync(storage, tasks, consoleIO);
    }
}
