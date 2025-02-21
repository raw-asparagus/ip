package dusk.ui;

/**
 * Represents a response containing a message and a response type.
 */
public class DuskResponse {

    private final String message;
    private final ResponseType type;

    /**
     * Constructs a DuskResponse with the specified message and response type.
     *
     * @param message the response message
     * @param type    the type of the response
     */
    public DuskResponse(String message, ResponseType type) {
        this.message = message;
        this.type = type;
    }

    /**
     * Returns the response message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the response type.
     *
     * @return the type of the response
     */
    public ResponseType getType() {
        return type;
    }

    /**
     * Enumeration of response types.
     */
    public enum ResponseType {
        NORMAL,
        ERROR,
        SYSTEM_ERROR
    }
}
