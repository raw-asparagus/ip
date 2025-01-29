package command;

import task.Task;
import task.TaskList;
import task.TaskListException;
import ui.ConsoleIO;

import java.io.IOException;

public class ListCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;

    public ListCommand(TaskList tasks, ConsoleIO consoleIO) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
    }

    @Override
    public void execute() throws IOException, TaskListException {
        if (tasks.isEmpty()) {
            consoleIO.print("Task list is empty!");
        } else {
            String[] messages = new String[tasks.size() + 1];
            messages[0] = "Here are the tasks in your list:";
            for (int i = 1; i <= tasks.size(); i++) {
                Task task;
                try {
                    task = tasks.getTask(i - 1);
                } catch (Exception e) {
                    throw new TaskListException(e.getMessage());
                }
                messages[i] = i + "." + task;
            }
            consoleIO.print(messages);
        }
    }
}
