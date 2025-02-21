package dusk;

/**
 * Represents exceptions specific to Dusk application logic.
 */
public class DuskException extends Exception {

    private final DuskExceptionType duskExceptionType;

    /**
     * Constructs a new DuskException with the specified detail message and exception type.
     *
     * @param message the detail message.
     * @param duskExceptionType the type of exception.
     */
    public DuskException(String message, DuskExceptionType duskExceptionType) {
        super(message);
        this.duskExceptionType = duskExceptionType;
    }

    /**
     * Returns the type of the exception.
     *
     * @return the dusk exception type.
     */
    public DuskExceptionType getErrorType() {
        return duskExceptionType;
    }
}
