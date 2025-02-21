package dusk.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of tasks, providing methods for adding, removing, and
 * retrieving tasks based on various conditions.
 */
public class TaskList {

    /**
     * The underlying collection of Task objects associated with this TaskList.
     */
    private final List<Task> tasks;

    /**
     * Creates a new TaskList instance with an empty internal list of tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a new task to the list.
     *
     * @param task the Task object to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task at the specified index from the list.
     *
     * @param index the 0-based position of the task to remove
     * @return the removed Task object
     * @throws TaskListException if the provided index is out of bounds
     */
    public Task removeTask(int index) throws TaskListException {
        try {
            return tasks.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListException("Index out of bounds: " + (index + 1));
        }
    }

    /**
     * Marks the specified task as done.
     *
     * @param index the 0-based position of the task to mark
     * @throws TaskListException if the index is out of bounds
     * @throws MarkTaskException if the task is already marked as done
     */
    public void markTask(int index) throws TaskListException, MarkTaskException {
        try {
            Task task = getTask(index);
            if (task.getDone()) {
                throw new MarkTaskException("Task already marked as done!");
            }
            task.markDone();
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListException("Index out of bounds: " + (index + 1));
        }
    }

    /**
     * Marks the specified task as not done.
     *
     * @param index the 0-based position of the task to unmark
     * @throws TaskListException if the index is out of bounds
     * @throws MarkTaskException if the task is already marked as not done
     */
    public void unmarkTask(int index) throws TaskListException, MarkTaskException {
        try {
            Task task = getTask(index);
            if (!task.getDone()) {
                throw new MarkTaskException("Task already marked as not done!");
            }
            task.markUndone();
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListException("Index out of bounds: " + (index + 1));
        }
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index the 0-based position of the task
     * @return the Task object at the given index
     * @throws TaskListException if the index is out of bounds
     */
    public Task getTask(int index) throws TaskListException {
        try {
            return tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListException("Index out of bounds: " + (index + 1));
        }
    }

    /**
     * Retrieves the total number of tasks in the list.
     *
     * @return the size of the internal task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if there are no tasks in the list, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Searches for tasks based on a keyword and/or date criteria.
     * If keyword is provided, filters tasks whose descriptions contain the keyword.
     * If date is provided, filters tasks occurring on that specific date.
     * If both fromDate and toDate are provided, filters tasks within that date range.
     *
     * @param keyword the search keyword (optional, can be null)
     * @param date specific date to search for tasks (optional, can be null)
     * @param fromDate start date of the range (optional, can be null)
     * @param toDate end date of the range (optional, can be null)
     * @return a TaskList containing tasks that match all provided criteria
     */
    public TaskList search(String keyword, LocalDateTime date,
                           LocalDateTime fromDate, LocalDateTime toDate) {
        TaskList result = new TaskList();

        for (Task task : tasks) {
            boolean matches = true;

            // Apply keyword filter if provided
            if (keyword != null && !keyword.isEmpty()) {
                matches = task.getName().toLowerCase()
                        .contains(keyword.toLowerCase());
            }

            // Apply date filter if provided
            if (matches && date != null) {
                matches = isOnSpecificDate(task, date);
            }

            // Apply date range filter if both fromDate and toDate are provided
            if (matches && fromDate != null && toDate != null) {
                matches = isWithinDateRange(task, fromDate, toDate);
            }

            if (matches) {
                result.addTask(task);
            }
        }

        return result;
    }

    /**
     * Checks if a task falls within the specified date range.
     *
     * @param task the task to check
     * @param fromDate start of the date range
     * @param toDate end of the date range
     * @return true if the task is within the date range, false otherwise
     */
    private boolean isWithinDateRange(Task task, LocalDateTime fromDate, LocalDateTime toDate) {
        if (task instanceof Event event) {
            return !event.getTo().isBefore(fromDate) && !event.getFrom().isAfter(toDate);
        } else if (task instanceof Deadline deadline) {
            LocalDateTime dueDate = deadline.getBy();
            return !dueDate.isBefore(fromDate) && !dueDate.isAfter(toDate);
        }
        return false;  // ToDo tasks don't have dates
    }

    /**
     * Checks if a task occurs on a specific date.
     *
     * @param task the task to check
     * @param date the date to check against
     * @return true if the task occurs on the specified date, false otherwise
     */
    private boolean isOnSpecificDate(Task task, LocalDateTime date) {
        if (task instanceof Event event) {
            return event.getFrom().toLocalDate().equals(date.toLocalDate()) ||
                    event.getTo().toLocalDate().equals(date.toLocalDate());
        } else if (task instanceof Deadline deadline) {
            return deadline.getBy().toLocalDate().equals(date.toLocalDate());
        }
        return false;  // ToDo tasks don't have dates
    }
}
