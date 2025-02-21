package dusk.task;

import dusk.ui.DuskException;
import dusk.ui.DuskExceptionType;

/**
 * Represents an exception thrown when there is an error marking a task
 * as completed or not completed.
 */
public class MarkTaskException extends DuskException {

    /**
     * Constructs a MarkTaskException with a specified detail message.
     *
     * @param message the detail message
     */
    public MarkTaskException(String message) {
        super(message, DuskExceptionType.TASK_ERROR);
    }
}
