package dusk.command;

/**
 * Represents an exception that occurs due to invalid user input.
 */
public class InputException extends Exception {

    /**
     * Constructs an InputException with the specified message.
     *
     * @param message the detail message
     */
    public InputException(String message) {
        super(message);
    }
}