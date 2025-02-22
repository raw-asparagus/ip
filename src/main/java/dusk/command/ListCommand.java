package dusk.command;

import java.io.IOException;
import java.time.LocalDateTime;

import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.DuskIO;

/**
 * Command for listing tasks according to various date filters.
 */
public class ListCommand extends Command {

    private final TaskList tasks;
    private final DuskIO duskIO;
    private final LocalDateTime onDate;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    /**
     * Constructs a ListCommand.
     *
     * @param tasks    the current task list
     * @param duskIO   the I/O interface
     * @param onDate   filter tasks on this specific date (nullable)
     * @param fromDate filter tasks from this start date (nullable)
     * @param toDate   filter tasks until this end date (nullable)
     */
    public ListCommand(TaskList tasks, DuskIO duskIO, LocalDateTime onDate,
                       LocalDateTime fromDate, LocalDateTime toDate) {
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
            filteredTasks = tasks.search(null, onDate, null, null);
            header = "Here are the tasks on " + onDate.toLocalDate() + ":";
        } else if (fromDate != null && toDate != null) {
            filteredTasks = tasks.search(null, null, fromDate, toDate);
            header = "Here are the tasks between " + fromDate + " and " + toDate + ":";
        } else if (fromDate != null || toDate != null) {
            throw new InputException("Both /from and /to must be specified together.");
        } else {
            filteredTasks = tasks;
            header = "Here are all the tasks:";
        }
        printTasks(filteredTasks, header);
    }

    /**
     * Prints the provided task list with a header.
     *
     * @param list   the task list to print
     * @param header the header message to display
     * @throws IOException       if an I/O error occurs
     * @throws TaskListException if task retrieval fails
     */
    private void printTasks(TaskList list, String header) throws IOException, TaskListException {
        if (list.isEmpty()) {
            duskIO.print("No matching tasks found!");
            return;
        }

        String[] messages = new String[list.size() + 1];
        messages[0] = header;
        for (int i = 0; i < list.size(); i++) {
            messages[i + 1] = (i + 1) + ". " + list.getTask(i);
        }
        duskIO.print(messages);
    }
}
