package dusk.task;

public class Deadline extends Task {
    private final String by;

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
        return "[D]" + super.toString() + " (by: " + (getBy().isEmpty() ? "no idea :p" : getBy()) + ")";
    }
}
