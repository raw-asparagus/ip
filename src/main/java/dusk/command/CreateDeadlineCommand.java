package dusk.command;

import dusk.storage.Storage;
import dusk.task.Deadline;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Creates a {@code Deadline} task with a specified description and due date/time,
 * and adds it to the task list.
 */
public class CreateDeadlineCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final LocalDateTime by;

    /**
     * Constructs a CreateDeadlineCommand.
     *
     * @param tasks       the task list to which the new deadline task will be added
     * @param consoleIO   the console I/O for interaction
     * @param storage     the storage facility for saving task data
     * @param description the description of the new deadline task
     * @param by          the deadline date/time for the task
     */
    public CreateDeadlineCommand(TaskList tasks, ConsoleIO consoleIO,
                                 Storage storage, String description, LocalDateTime by) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.by = by;
    }

    /**
     * Creates a new {@link Deadline} with the provided description and due date/time,
     * adds it to the task list, displays a confirmation message, and saves the tasks asynchronously.
     *
     * @throws IOException    if an I/O error occurs while printing to the console
     * @throws InputException if the description is empty
     */
    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("A 'DEADLINE' command must include a description.");
        }

        Deadline newTask = new Deadline(description, by);
        tasks.addTask(newTask);

        consoleIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}