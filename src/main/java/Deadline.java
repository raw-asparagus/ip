public class Deadline extends Task {
    protected String by;

    public Deadline(String desc, String by) {
        super(desc);
        this.by = by;
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
