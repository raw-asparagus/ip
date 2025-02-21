package dusk.command;

import java.io.IOException;
import java.time.LocalDateTime;

import dusk.storage.Storage;
import dusk.task.Event;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

/**
 * Command to create an event task.
 */
public class CreateEventCommand extends Command {

    private final TaskList tasks;
    private final DuskIO duskIO;
    private final Storage storage;
    private final String description;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Constructs a CreateEventCommand.
     *
     * @param tasks       the current task list
     * @param duskIO      the I/O interface
     * @param storage     the storage handler
     * @param description the event description
     * @param startTime   the start time of the event
     * @param endTime     the end time of the event
     */
    public CreateEventCommand(TaskList tasks, DuskIO duskIO, Storage storage,
                              String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.tasks = tasks;
        this.duskIO = duskIO;
        this.storage = storage;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("An event command must include a description.");
        }

        Event newTask = new Event(description, startTime, endTime);
        tasks.addTask(newTask);

        duskIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks);
    }
}
