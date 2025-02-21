package dusk;

public class DuskException extends Exception {

    private final DuskExceptionType duskExceptionType;

    public DuskException(String message, DuskExceptionType duskExceptionType) {
        super(message);
        this.duskExceptionType = duskExceptionType;
    }

    public DuskExceptionType getErrorType() {
        return duskExceptionType;
    }
}
