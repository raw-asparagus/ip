package command;

import storage.Storage;
import task.TaskList;
import task.Todo;
import ui.ConsoleIO;

import java.io.IOException;

public class CreateTodoCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;

    public CreateTodoCommand(TaskList tasks, ConsoleIO consoleIO, Storage storage, String description) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("A 'TODO' command must include a description.");
        }
        Todo newTask = new Todo(description);
        tasks.addTask(newTask);

        consoleIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        saveAsync(storage, tasks, consoleIO);
    }
}