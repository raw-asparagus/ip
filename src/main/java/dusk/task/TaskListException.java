package dusk.task;

import dusk.DuskException;
import dusk.DuskExceptionType;

/**
 * Exception thrown when processing tasks in the TaskList.
 */
public class TaskListException extends DuskException {

    /**
     * Constructs a TaskListException with the specified detail message.
     *
     * @param message the detail message
     */
    public TaskListException(String message) {
        super(message, DuskExceptionType.TASK_ERROR);
    }
}
