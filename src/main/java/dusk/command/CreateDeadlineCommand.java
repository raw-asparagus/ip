package dusk.command;

import dusk.storage.Storage;
import dusk.task.Deadline;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Creates a new deadline task with a specified description and date/time.
 */
public class CreateDeadlineCommand extends Command {

    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final LocalDateTime deadline;

    /**
     * Constructs a command for creating a deadline task.
     *
     * @param tasks       the current task list
     * @param consoleIO   the console I/O
     * @param storage     the storage object
     * @param description the description of the new task
     * @param deadline    the date/time by which the task is due
     */
    public CreateDeadlineCommand(TaskList tasks, ConsoleIO consoleIO,
                                 Storage storage, String description, LocalDateTime deadline) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.deadline = deadline;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("A 'DEADLINE' command must include a description.");
        }

        Deadline newTask = new Deadline(description, deadline);
        tasks.addTask(newTask);

        consoleIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}