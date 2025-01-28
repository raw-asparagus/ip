import task.Task;
import ui.ConsoleIO;

import java.io.IOException;
import java.util.List;

public class ListCommand implements Command {
    private final List<Task> tasks;
    private final ConsoleIO consoleIO;

    public ListCommand(List<Task> tasks, ConsoleIO consoleIO) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
    }

    @Override
    public void execute() throws IOException {
        if (tasks.isEmpty()) {
            consoleIO.print("task.Task list is empty!");
        } else {
            String[] messages = new String[tasks.size() + 1];
            messages[0] = "Here are the tasks in your list:";
            for (int i = 1; i <= tasks.size(); i++) {
                messages[i] = i + "." + tasks.get(i - 1);
            }
            consoleIO.print(messages);
        }
    }
}