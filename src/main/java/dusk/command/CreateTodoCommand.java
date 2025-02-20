package dusk.command;

import java.io.IOException;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.task.Todo;
import dusk.ui.DuskIO;

/**
 * Creates a new todo task with a specified description.
 */
public class CreateTodoCommand extends Command {

    private final TaskList tasks;
    private final DuskIO duskIO;
    private final Storage storage;
    private final String description;

    /**
     * Constructs a command for creating a todo task.
     *
     * @param tasks       the current task list
     * @param duskIO      the console I/O
     * @param storage     the storage object
     * @param description the description of the todo
     */
    public CreateTodoCommand(TaskList tasks, DuskIO duskIO, Storage storage, String description) {
        this.tasks = tasks;
        this.duskIO = duskIO;
        this.storage = storage;
        this.description = description;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("A 'TODO' command must include a description.");
        }
        Todo newTask = new Todo(description);
        tasks.addTask(newTask);

        duskIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, duskIO);
    }
}
