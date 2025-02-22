package dusk.command;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.task.Todo;
import dusk.ui.DuskIO;

/**
 * Command to create a {@link Todo} task.
 */
public class CreateTodoCommand extends CreateTaskCommand {

    /**
     * Constructs a CreateTodoCommand.
     *
     * @param tasks       the task list
     * @param duskIO      the UI interface
     * @param storage     the storage handler
     * @param description the task description
     */
    public CreateTodoCommand(TaskList tasks, DuskIO duskIO, Storage storage, String description) {
        super(tasks, duskIO, storage, description);
    }

    /**
     * Creates a Todo task.
     *
     * @return a new {@link Todo} instance
     */
    @Override
    protected dusk.task.Task createTask() {
        return new Todo(description);
    }

    /**
     * Returns the validation message for a todo command.
     *
     * @return the validation message
     */
    @Override
    protected String getValidationMessage() {
        return "A todo command must include a description.";
    }
}
