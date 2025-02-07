package dusk.command;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.task.Todo;
import dusk.ui.ConsoleIO;

import java.io.IOException;

/**
 * Creates a {@code Todo} task with a specified description
 * and adds it to the task list.
 */
public class CreateTodoCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;

    /**
     * Constructs a CreateTodoCommand.
     *
     * @param tasks       the task list to which the new to-do task will be added
     * @param consoleIO   the console I/O for interaction
     * @param storage     the storage facility for saving task data
     * @param description the description of the new to-do task
     */
    public CreateTodoCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage, String description) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
    }

    /**
     * Creates a new {@link Todo} with the provided description, adds it to the task list,
     * displays a confirmation message, and saves the tasks asynchronously.
     *
     * @throws IOException    if an I/O error occurs while printing to the console
     * @throws InputException if the description is empty
     */
    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("A 'TODO' dusk.command must include a description.");
        }
        Todo newTask = new Todo(description);
        tasks.addTask(newTask);

        consoleIO.print(
                "Got it. I've added this dusk.task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}