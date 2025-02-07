package dusk.command;

import dusk.storage.Storage;
import dusk.task.Event;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Creates an {@code Event} task with a specified description, start time,
 * and end time, and adds it to the task list.
 */
public class CreateEventCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs a CreateEventCommand.
     *
     * @param tasks       the task list to which the new event task will be added
     * @param consoleIO   the console I/O for interaction
     * @param storage     the storage facility for saving task data
     * @param description the description of the new event
     * @param from        the start date/time of the event
     * @param to          the end date/time of the event
     */
    public CreateEventCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage,
                              String description, LocalDateTime from, LocalDateTime to) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.from = from;
        this.to = to;
    }

    /**
     * Creates a new {@link Event} with the provided description, start, and end
     * date/time, adds it to the task list, displays a confirmation message, and saves
     * the tasks asynchronously.
     *
     * @throws IOException    if an I/O error occurs while printing to the console
     * @throws InputException if the description is empty
     */
    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("An 'EVENT' command must include a description.");
        }

        Event newTask = new Event(description, from, to);
        tasks.addTask(newTask);

        consoleIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}