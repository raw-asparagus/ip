package dusk.command;

import java.time.LocalDateTime;

import dusk.storage.Storage;
import dusk.task.Deadline;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

/**
 * Command to create a {@link Deadline} task.
 */
public class CreateDeadlineCommand extends CreateTaskCommand {

    private final LocalDateTime deadline;

    /**
     * Constructs a CreateDeadlineCommand.
     *
     * @param tasks      the task list
     * @param duskIO     the UI interface
     * @param storage    the storage handler
     * @param description the task description
     * @param deadline   the deadline of the task
     */
    public CreateDeadlineCommand(TaskList tasks, DuskIO duskIO,
                                 Storage storage, String description, LocalDateTime deadline) {
        super(tasks, duskIO, storage, description);
        this.deadline = deadline;
    }

    /**
     * Creates a Deadline task.
     *
     * @return a new {@link Deadline} instance
     */
    @Override
    protected dusk.task.Task createTask() {
        return new Deadline(description, deadline);
    }

    /**
     * Returns the validation message for a deadline command.
     *
     * @return the validation message
     */
    @Override
    protected String getValidationMessage() {
        return "A deadline command must include a description.";
    }
}
