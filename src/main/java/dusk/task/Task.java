package dusk.task;

/**
 * An abstract class representing a general task with a description
 * and completion status.
 */
public abstract class Task {

    /**
     * The text description of this task.
     */
    private final String description;

    /**
     * Indicates whether this task has been marked as done.
     */
    private boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param desc the text description for the task
     */
    public Task(String desc) {
        this.description = desc;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markUndone() {
        this.isDone = false;
    }

    /**
     * Retrieves the description of this task.
     *
     * @return the task description as a string
     */
    public String getName() {
        return description;
    }

    /**
     * Checks whether this task is marked as done.
     *
     * @return true if the task is done, false otherwise
     */
    public boolean getDone() {
        return isDone;
    }

    /**
     * Returns a string representation of this task, showing whether
     * it is done ([X]) or not ([ ]) along with its description.
     *
     * @return a formatted string representation of the task
     */
    @Override
    public String toString() {
        return "[" + (getDone() ? "X" : " ") + "] " + getName();
    }
}