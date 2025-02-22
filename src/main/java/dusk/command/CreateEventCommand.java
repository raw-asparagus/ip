package dusk.command;

import java.time.LocalDateTime;

import dusk.storage.Storage;
import dusk.task.Event;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

/**
 * Command to create an {@link Event} task.
 */
public class CreateEventCommand extends CreateTaskCommand {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Constructs a CreateEventCommand.
     *
     * @param tasks       the task list
     * @param duskIO      the UI interface
     * @param storage     the storage handler
     * @param description the task description
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     */
    public CreateEventCommand(TaskList tasks, DuskIO duskIO, Storage storage,
                              String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(tasks, duskIO, storage, description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Creates an Event task.
     *
     * @return a new {@link Event} instance
     */
    @Override
    protected dusk.task.Task createTask() {
        return new Event(description, startTime, endTime);
    }

    /**
     * Returns the validation message for an event command.
     *
     * @return the validation message
     */
    @Override
    protected String getValidationMessage() {
        return "An event command must include a description.";
    }
}
