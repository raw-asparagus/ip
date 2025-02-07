package dusk.task;

/**
 * Represents a basic to-do task with only a description and a completion status.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description the description of the to-do task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this to-do task,
     * including its type and completion status.
     *
     * @return a string showing the task type and status
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}