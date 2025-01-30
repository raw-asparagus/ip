package dusk.task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task removeTask(int index) throws TaskListException {
        try {
            return tasks.remove(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListException("Index out of bounds: " + (index + 1));
        }
    }

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

    public Task getTask(int index) throws TaskListException {
        try {
            return tasks.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListException("Index out of bounds: " + (index + 1));
        }
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

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
     * Returns a new TaskList containing only tasks that occur on the specified date.
     * Actual “on-date” checking is done in Event/Deadline themselves (e.g., isOnDate).
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