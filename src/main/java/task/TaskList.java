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
        tasks.add(task);
    }

    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    public void markTask(int index) throws TaskListException, MarkTaskException {
        Task task = getTask(index);     // getTask: TaskList Exception
        if (task.getDone()) {
            throw new MarkTaskException("Task already marked as done!");
        }
        task.markDone();
    }

    public void unmarkTask(int index) throws TaskListException, MarkTaskException {
        Task task = getTask(index);     // getTask: TaskList Exception
        if (!task.getDone()) {
            throw new MarkTaskException("Task already marked as not done");
        }
        task.markUndone();
    }

    // Accessors
    public Task getTask(int index) throws TaskListException {
        Task task;
        try {
            task = tasks.get(index);
            return task;
        } catch (IndexOutOfBoundsException e) {
            throw new TaskListException("Index out of bounds: " + index);
        }
    }

    // For use in storage
    public static Task getTask(String[] parts) throws TaskListException {
        String taskType = parts[0];
        boolean done = Boolean.parseBoolean(parts[1]);
        String description = parts[2];

        Task task = switch (taskType) {
            case "E" -> parts.length >= 5 ? new Event(description, parts[3], parts[4]) : null;
            case "D" -> parts.length >= 4 ? new Deadline(description, parts[3]) : null;
            case "T" -> new Todo(description);
            default -> null;
        };

        if (task == null) {
            throw new TaskListException("Unknown task type: " + taskType + "|" + description + "|" + done);
        }

        if (done) {
            task.markDone();
        }
        return task;
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
