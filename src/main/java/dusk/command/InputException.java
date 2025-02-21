package dusk.command;

import dusk.ui.DuskException;
import dusk.ui.DuskExceptionType;

/**
 * Represents an exception that occurs due to invalid user input.
 */
public class InputException extends DuskException {

    /**
     * Constructs an InputException with the specified message.
     *
     * @param message the detail message
     */
    public InputException(String message) {
        super(message, DuskExceptionType.INPUT_ERROR);
    }

}
