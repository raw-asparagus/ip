public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        String by;
        if (this.by.isEmpty()) {
            by = "no idea :p";
        } else {
            by = this.by;
        }
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
