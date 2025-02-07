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
     * @throws TaskListException   if the index is out of bounds
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
     * @throws TaskListException   if the index is out of bounds
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
     * Produces a new TaskList containing all tasks (of type Deadline or Event)
     * whose date/time is within the specified range.
     *
     * @param start the start date/time of the range
     * @param end   the end date/time of the range
     * @return a TaskList containing the tasks that fall within the given range
     */
    public TaskList getTasksWithin(LocalDateTime start, LocalDateTime end) {
        TaskList result = new TaskList();
        for (Task task : tasks) {
            if (task instanceof Event e) {
                if (e.isWithinRange(start, end)) {
                    result.addTask(e);
                }
            } else if (task instanceof Deadline d) {
                if (d.isWithinRange(start, end)) {
                    result.addTask(d);
                }
            }
        }
        return result;
    }

    /**
     * Produces a new TaskList containing all tasks (of type Event or Deadline)
     * that occur on the specified date.
     *
     * @param date the LocalDateTime whose date is to be compared
     * @return a TaskList containing tasks that occur on the specified date
     */
    public TaskList getTasksOn(LocalDateTime date) {
        TaskList result = new TaskList();
        for (Task task : tasks) {
            if (task instanceof Event e) {
                if (e.isOnDate(date)) {
                    result.addTask(e);
                }
            } else if (task instanceof Deadline d) {
                if (d.isOnDate(date)) {
                    result.addTask(d);
                }
            }
        }
        return result;
    }
}