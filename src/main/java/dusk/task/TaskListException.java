package dusk.task;

import dusk.DuskException;
import dusk.DuskExceptionType;

/**
 * Represents an exception thrown when there is an error processing tasks
 * within a TaskList.
 */
public class TaskListException extends DuskException {

    /**
     * Constructs a TaskListException with a specified detail message.
     *
     * @param message the detail message
     */
    public TaskListException(String message) {
        super(message, DuskExceptionType.TASK_ERROR
        );
    }
}
