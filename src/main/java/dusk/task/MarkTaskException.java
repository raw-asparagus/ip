package dusk.task;

/**
 * Represents an exception thrown when there is an error marking a task
 * as completed or not completed.
 */
public class MarkTaskException extends Exception {

    /**
     * Constructs a MarkTaskException with a specified detail message.
     *
     * @param message the detail message
     */
    public MarkTaskException(String message) {
        super(message);
    }
}
