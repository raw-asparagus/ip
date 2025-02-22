package dusk.command;

import java.io.IOException;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

/**
 * Abstract command for creating tasks.
 */
public abstract class CreateTaskCommand extends Command {

    protected final TaskList tasks;
    protected final DuskIO duskIO;
    protected final Storage storage;
    protected final String description;

    /**
     * Constructs a CreateTaskCommand.
     *
     * @param tasks       the task list
     * @param duskIO      the UI interface
     * @param storage     the storage handler
     * @param description the task description
     */
    protected CreateTaskCommand(TaskList tasks, DuskIO duskIO, Storage storage, String description) {
        this.tasks = tasks;
        this.duskIO = duskIO;
        this.storage = storage;
        this.description = description;
    }

    /**
     * Executes the command to create a task and persists the change.
     *
     * @throws IOException     if an I/O error occurs
     * @throws InputException if the input is invalid
     */
    @Override
    public void execute() throws IOException, InputException {
        validateDescription();
        var newTask = createTask();
        tasks.addTask(newTask);
        printConfirmation(newTask);
        saveAsync(storage, tasks);
    }

    /**
     * Validates the task description.
     *
     * @throws InputException if the description is empty
     */
    protected void validateDescription() throws InputException {
        if (description.isEmpty()) {
            throw new InputException(getValidationMessage());
        }
    }

    /**
     * Prints a confirmation message after adding the task.
     *
     * @param newTask the newly created task
     * @throws IOException if an I/O error occurs while printing
     */
    protected void printConfirmation(dusk.task.Task newTask) throws IOException {
        duskIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );
    }

    /**
     * Creates a task.
     *
     * @return the created task
     */
    protected abstract dusk.task.Task createTask();

    /**
     * Returns the validation message for an empty description.
     *
     * @return the validation message
     */
    protected abstract String getValidationMessage();
}
