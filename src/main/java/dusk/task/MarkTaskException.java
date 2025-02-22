package dusk.task;

import dusk.DuskException;
import dusk.DuskExceptionType;

/**
 * Exception thrown when an error occurs while marking a task.
 */
public class MarkTaskException extends DuskException {

    /**
     * Constructs a MarkTaskException with the specified message.
     *
     * @param message the detail message
     */
    public MarkTaskException(String message) {
        super(message, DuskExceptionType.TASK_ERROR);
    }
}
