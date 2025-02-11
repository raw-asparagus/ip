package dusk.command;

import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Lists tasks in various ways based on the provided arguments
 * (e.g., tasks on a certain date, tasks within a date range, or all tasks).
 */
public class ListCommand extends Command {

    private final TaskList tasks;
    private final ConsoleIO consoleIO;
    private final LocalDateTime onDate;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    /**
     * Constructs a command for listing tasks.
     *
     * @param tasks     the current task list
     * @param consoleIO the console I/O
     * @param onDate    date to filter tasks exactly on
     * @param fromDate  start date to filter tasks
     * @param toDate    end date to filter tasks
     */
    public ListCommand(TaskList tasks,
                       ConsoleIO consoleIO,
                       LocalDateTime onDate,
                       LocalDateTime fromDate,
                       LocalDateTime toDate) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.onDate = onDate;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public void execute() throws IOException, TaskListException {
        if (tasks.isEmpty()) {
            consoleIO.print("Task list is empty!");
            return;
        }

        if (onDate != null) {
            TaskList onList = tasks.getTasksOn(onDate);
            printTasks(onList, "Here are the tasks on " + onDate.toLocalDate() + ":");
            return;
        }

        if (fromDate != null && toDate != null) {
            TaskList withinList = tasks.getTasksWithin(fromDate, toDate);
            printTasks(withinList,
                    "Here are the tasks between " + fromDate + " and " + toDate + ":");
            return;
        } else if (fromDate != null || toDate != null) {
            throw new TaskListException("Both /from and /to must be specified together.");
        }

        printTasks(tasks, "Here are all the tasks:");
    }

    private void printTasks(TaskList list, String header) throws IOException, TaskListException {
        if (list.isEmpty()) {
            consoleIO.print("No matching tasks found!");
            return;
        }

        String[] messages = new String[list.size() + 1];
        messages[0] = header;

        for (int i = 0; i < list.size(); i++) {
            Task task = list.getTask(i);
            messages[i + 1] = (i + 1) + "." + task;
        }
        consoleIO.print(messages);
    }
}