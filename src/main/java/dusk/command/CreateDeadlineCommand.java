package dusk.command;

import java.io.IOException;
import java.time.LocalDateTime;

import dusk.storage.Storage;
import dusk.task.Deadline;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

/**
 * Command to create a deadline task.
 */
public class CreateDeadlineCommand extends Command {

    private final TaskList tasks;
    private final DuskIO duskIO;
    private final Storage storage;
    private final String description;
    private final LocalDateTime deadline;

    /**
     * Constructs a CreateDeadlineCommand.
     *
     * @param tasks       the current task list
     * @param duskIO      the I/O interface
     * @param storage     the storage handler
     * @param description the task description
     * @param deadline    the deadline for the task
     */
    public CreateDeadlineCommand(TaskList tasks, DuskIO duskIO, Storage storage,
                                 String description, LocalDateTime deadline) {
        this.tasks = tasks;
        this.duskIO = duskIO;
        this.storage = storage;
        this.description = description;
        this.deadline = deadline;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("A deadline command must include a description.");
        }

        Deadline newTask = new Deadline(description, deadline);
        tasks.addTask(newTask);

        duskIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks);
    }
}
