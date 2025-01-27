public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

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
