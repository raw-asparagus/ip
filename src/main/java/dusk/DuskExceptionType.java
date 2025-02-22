package dusk;

/**
 * Enumerates the types of exceptions that can be thrown in the Dusk application.
 */
public enum DuskExceptionType {
    COMMAND_ERROR("Command Error"),
    INPUT_ERROR("Input Error"),
    STORAGE_ERROR("Storage Error"),
    TASK_ERROR("Task Error"),
    SYSTEM_ERROR("System Error");

    private final String label;

    /**
     * Constructs an exception type with the specified label.
     *
     * @param label the label for the exception type.
     */
    DuskExceptionType(String label) {
        this.label = label;
    }

    /**
     * Returns the label associated with the exception type.
     *
     * @return the label for the exception type.
     */
    public String getLabel() {
        return label;
    }
}
