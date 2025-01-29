package task;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    // Constructor
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    // Mutators
    public void addTask(Task task) {
        getTasks().add(task);
    }

    public Task removeTask(int index) throws TaskListException {
        try {
            return getTasks().remove(index);
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

    // Accessors
    private List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(int index) throws TaskListException {
        try {
            return getTasks().get(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListException("Index out of bounds: " + (index + 1));
        }
    }

    public int size() {
        return getTasks().size();
    }

    public boolean isEmpty() {
        return getTasks().isEmpty();
    }
}
