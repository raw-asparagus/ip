package dusk.command;

import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Lists all tasks or filters tasks by date/time criteria.
 * Can display all tasks, tasks on a specific date, or tasks within
 * a date/time range.
 */
public class ListCommand extends Command {
    private final TaskList tasks;
    private final ConsoleIO consoleIO;

    private final LocalDateTime onDate;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;

    /**
     * Constructs a ListCommand.
     *
     * @param tasks     the task list whose tasks are to be displayed
     * @param consoleIO the console I/O for interaction
     * @param onDate    the date to filter tasks (optional)
     * @param fromDate  the start of the date/time range to filter tasks (optional)
     * @param toDate    the end of the date/time range to filter tasks (optional)
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

    /**
     * Displays all tasks or any filtered subset based on the date/time criteria,
     * and prints them via the console.
     *
     * @throws IOException       if an I/O error occurs while printing to the console
     * @throws TaskListException if date/time filters are invalid or cause errors
     */
    @Override
    public void execute() throws IOException, TaskListException {
        if (tasks.isEmpty()) {
            consoleIO.print("Task list is empty!");
            return;
        }

        if (onDate != null) {
            TaskList onList = tasks.getTasksOn(onDate);
            printTasks(onList, "Here are the tasks on " + onDate.toLocalDate().toString() + ":");
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

    /**
     * Prints the tasks in the given {@link TaskList} along with a header message.
     *
     * @param list   the TaskList containing tasks to be displayed
     * @param header the header message to print before listing tasks
     * @throws IOException       if an I/O error occurs while printing to the console
     * @throws TaskListException if the TaskList is invalid
     */
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