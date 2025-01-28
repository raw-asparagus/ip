package task;

public class Deadline extends Task {
    protected final String by;

    // Constructor
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    // Accessor
    public String getBy() {
        return by;
    }

    @Override
    public String toString() {
        String by = this.by;
        if (by.isEmpty()) {
            by = "no idea :p";
        }
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
