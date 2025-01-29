package dusk.command;

import dusk.storage.Storage;
import dusk.task.Event;
import dusk.task.TaskList;
import dusk.ui.ConsoleIO;

import java.io.IOException;

public class CreateEventCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final String from;
    private final String to;

    public CreateEventCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage,
                              String description, String from, String to) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("An 'EVENT' dusk.command must include a description.");
        }
        Event newTask = new Event(description, from, to);
        tasks.addTask(newTask);

        consoleIO.print(
                "Got it. I've added this dusk.task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}
