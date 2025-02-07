package dusk.command;

import dusk.storage.Storage;
import dusk.task.Event;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Creates a new event task with a specified time range.
 */
public class CreateEventCommand extends Command {

    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    /**
     * Constructs a command for creating an event task.
     *
     * @param tasks       the current task list
     * @param consoleIO   the console I/O
     * @param storage     the storage object
     * @param description the description of the event
     * @param startTime   the start date/time of the event
     * @param endTime     the end date/time of the event
     */
    public CreateEventCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage,
                              String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("An 'EVENT' command must include a description.");
        }

        Event newTask = new Event(description, startTime, endTime);
        tasks.addTask(newTask);

        consoleIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}