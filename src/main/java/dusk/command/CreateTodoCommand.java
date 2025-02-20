package dusk.command;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.task.Todo;
import dusk.ui.ConsoleIO;

import java.io.IOException;

/**
 * Creates a new todo task with a specified description.
 */
public class CreateTodoCommand extends Command {

    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;

    /**
     * Constructs a command for creating a todo task.
     *
     * @param tasks       the current task list
     * @param consoleIO   the console I/O
     * @param storage     the storage object
     * @param description the description of the todo
     */
    public CreateTodoCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage, String description) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
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

        consoleIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}
