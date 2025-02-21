package dusk;

/**
 * Represents a response from Dusk with additional metadata about the response type.
 */
public class DuskResponse {
    private final String message;
    private final ResponseType type;

    public DuskResponse(String message, ResponseType type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public ResponseType getType() {
        return type;
    }

    public enum ResponseType {
        NORMAL,
        ERROR,
        SYSTEM_ERROR
    }
}
