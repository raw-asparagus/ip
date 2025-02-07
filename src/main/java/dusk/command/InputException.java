package dusk.command;

/**
 * Represents an exception related to invalid or insufficient user inputs
 * for a command.
 */
public class InputException extends Exception {

    /**
     * Constructs an {@code InputException} with the specified detail message.
     *
     * @param message the detail message
     */
    public InputException(String message) {
        super(message);
    }
}