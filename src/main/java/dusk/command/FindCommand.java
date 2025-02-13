package dusk.command;

import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;

/**
 * Represents a command that searches the task list for tasks whose
 * descriptions contain a given keyword.
 */
public class FindCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final String keyword;

    /**
     * Constructs a FindCommand.
     *
     * @param tasks     the task list to search through
     * @param consoleIO the console I/O for user interaction
     * @param keyword   the keyword to search for in task descriptions
     */
    public FindCommand(TaskList tasks, ConsoleIO consoleIO, String keyword) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.keyword = keyword;
    }

    /**
     * Executes the find command: searches for matching tasks and prints them.
     *
     * @throws IOException       if an I/O error occurs while printing to the console
     * @throws TaskListException if an error occurs while retrieving tasks
     */
    @Override
    public void execute() throws IOException, TaskListException {
        TaskList matchingTasks = tasks.search(keyword);

        if (matchingTasks.isEmpty()) {
            consoleIO.print("No matching tasks found!");
        } else {
            String header = "Here are the matching tasks in your list:";
            String[] messages = new String[matchingTasks.size() + 1];
            messages[0] = header;
            for (int i = 0; i < matchingTasks.size(); i++) {
                Task task = matchingTasks.getTask(i);
                messages[i + 1] = (i + 1) + "." + task;
            }
            consoleIO.print(messages);
        }
    }
}