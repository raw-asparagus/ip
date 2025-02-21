package dusk;

public enum DuskExceptionType {
    COMMAND_ERROR("Command Error"),
    INPUT_ERROR("Input Error"),
    STORAGE_ERROR("Storage Error"),
    TASK_ERROR("Task Error"),
    SYSTEM_ERROR("System Error");


    private final String label;

    DuskExceptionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
