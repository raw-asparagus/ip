package dusk.command;

import dusk.DuskException;
import dusk.DuskExceptionType;

/**
 * Exception thrown when there is an error in user input.
 */
public class InputException extends DuskException {

    /**
     * Constructs an InputException with the specified detail message.
     *
     * @param message the error detail message
     */
    public InputException(String message) {
        super(message, DuskExceptionType.INPUT_ERROR);
    }
}
