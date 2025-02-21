package dusk.command;

import java.io.IOException;
import java.time.LocalDateTime;

import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.DuskIO;

/**
 * Lists tasks in various ways based on the provided arguments
 * (e.g., tasks on a certain date, tasks within a date range, or all tasks).
 */
public class ListCommand extends Command {

    private final TaskList tasks;
    private final DuskIO duskIO;
    private final LocalDateTime onDate;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    /**
     * Constructs a command for listing tasks.
     *
     * @param tasks    the current task list
     * @param duskIO   the console I/O
     * @param onDate   date to filter tasks exactly on
     * @param fromDate start date to filter tasks
     * @param toDate   end date to filter tasks
     */
    public ListCommand(TaskList tasks,
                       DuskIO duskIO,
                       LocalDateTime onDate,
                       LocalDateTime fromDate,
                       LocalDateTime toDate) {
        this.tasks = tasks;
        this.duskIO = duskIO;
        this.onDate = onDate;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public void execute() throws IOException, TaskListException, InputException {
        if (tasks.isEmpty()) {
            duskIO.print("Task list is empty!");
            return;
        }

        TaskList filteredTasks;
        String header;

        if (onDate != null) {
            // Use search with only date parameter
            filteredTasks = tasks.search(null, onDate, null, null);
            header = "Here are the tasks on " + onDate.toLocalDate() + ":";
        } else if (fromDate != null && toDate != null) {
            // Use search with date range parameters
            filteredTasks = tasks.search(null, null, fromDate, toDate);
            header = "Here are the tasks between " + fromDate + " and " + toDate + ":";
        } else if (fromDate != null || toDate != null) {
            throw new InputException("Both /from and /to must be specified together.");
        } else {
            // No date filters, show all tasks
            filteredTasks = tasks;
            header = "Here are all the tasks:";
        }

        printTasks(filteredTasks, header);
    }

    private void printTasks(TaskList list, String header) throws IOException, TaskListException {
        if (list.isEmpty()) {
            duskIO.print("No matching tasks found!");
            return;
        }

        String[] messages = new String[list.size() + 1];
        messages[0] = header;

        for (int i = 0; i < list.size(); i++) {
            Task task = list.getTask(i);
            messages[i + 1] = (i + 1) + "." + task;
        }
        duskIO.print(messages);
    }
}
