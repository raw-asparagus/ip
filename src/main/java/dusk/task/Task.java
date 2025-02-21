package dusk.task;

/**
 * Abstract representation of a task with a description and completion status.
 */
public abstract class Task {

    private final String description;
    private boolean isDone;

    /**
     * Constructs a Task with the specified description.
     *
     * @param description the task description
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markUndone() {
        this.isDone = false;
    }

    /**
     * Retrieves the task description.
     *
     * @return the description text
     */
    public String getName() {
        return description;
    }

    /**
     * Checks whether the task is completed.
     *
     * @return true if done, false otherwise
     */
    public boolean getDone() {
        return isDone;
    }

    /**
     * Returns the string representation of this task.
     *
     * @return a formatted string indicating completion status and description
     */
    @Override
    public String toString() {
        return "[" + (isDone ? "X" : " ") + "] " + description;
    }
}
