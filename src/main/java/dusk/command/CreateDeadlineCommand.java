package dusk.command;

import dusk.storage.Storage;
import dusk.task.Deadline;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.time.LocalDateTime;

public class CreateDeadlineCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final LocalDateTime by;

    public CreateDeadlineCommand(TaskList tasks, ConsoleIO consoleIO,
                                 Storage storage, String description, LocalDateTime by) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.by = by;
    }

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
