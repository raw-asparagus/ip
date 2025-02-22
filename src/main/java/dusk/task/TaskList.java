package dusk.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of tasks.
 */
public class TaskList {

    private final List<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list.
     *
     * @param task the task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes the task at the specified index.
     *
     * @param index the index of the task to remove (0-based)
     * @return the removed task
     * @throws TaskListException if the index is out of bounds
     */
    public Task removeTask(int index) throws TaskListException {
        if (index < 0 || index >= tasks.size()) {
            throw new TaskListException("Invalid task index.");
        }
        return tasks.remove(index);
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index the index of the task to mark (0-based)
     * @throws TaskListException if the index is out of bounds
     * @throws MarkTaskException if the task is already marked as done
     */
    public void markTask(int index) throws TaskListException, MarkTaskException {
        Task task = getTask(index);
        if (task.getDone()) {
            throw new MarkTaskException("Task is already marked as done.");
        }
        task.markDone();
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index the index of the task to unmark (0-based)
     * @throws TaskListException if the index is out of bounds
     * @throws MarkTaskException if the task is already unmarked
     */
    public void unmarkTask(int index) throws TaskListException, MarkTaskException {
        Task task = getTask(index);
        if (!task.getDone()) {
            throw new MarkTaskException("Task is already unmarked.");
        }
        task.markUndone();
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index the index of the task (0-based)
     * @return the task at the given index
     * @throws TaskListException if the index is out of bounds
     */
    public Task getTask(int index) throws TaskListException {
        if (index < 0 || index >= tasks.size()) {
            throw new TaskListException("Invalid task index.");
        }
        return tasks.get(index);
    }

    /**
     * Returns the total number of tasks.
     *
     * @return the size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks whether the task list is empty.
     *
     * @return true if there are no tasks; false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Searches for tasks that match the given criteria.
     * If a keyword is provided, only tasks whose descriptions contain the keyword are returned.
     * If a specific date is provided, only tasks on that date (via isOnDate) are returned.
     * If both fromDate and toDate are provided, only tasks within the date range (via isWithinRange) are returned.
     *
     * @param keyword  search keyword (optional)
     * @param date     specific date to search for (optional)
     * @param fromDate start date of the range (optional)
     * @param toDate   end date of the range (optional)
     * @return a TaskList of tasks matching the criteria
     */
    public TaskList search(String keyword, LocalDateTime date,
                           LocalDateTime fromDate, LocalDateTime toDate) {
        TaskList result = new TaskList();
        for (Task task : tasks) {
            boolean matches = keyword == null || task.getDescription().toLowerCase().contains(keyword.toLowerCase());
            if (date != null) {
                boolean onDate = false;
                if (task instanceof Deadline) {
                    onDate = ((Deadline) task).isOnDate(date);
                } else if (task instanceof Event) {
                    onDate = ((Event) task).isOnDate(date);
                }
                if (!onDate) {
                    matches = false;
                }
            }
            if (fromDate != null && toDate != null) {
                boolean withinRange = false;
                if (task instanceof Deadline) {
                    withinRange = ((Deadline) task).isWithinRange(fromDate, toDate);
                } else if (task instanceof Event) {
                    withinRange = ((Event) task).isWithinRange(fromDate, toDate);
                }
                if (!withinRange) {
                    matches = false;
                }
            }
            if (matches) {
                result.addTask(task);
            }
        }
        return result;
    }
}
