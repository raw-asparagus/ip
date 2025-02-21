package dusk.task;

/**
 * Represents a basic to-do task.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task.
     *
     * @param description the task description
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of this to-do.
     *
     * @return a formatted string with task type and status
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
