public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String desc, String from, String to) {
        super(desc);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        String message;
        if (from.isEmpty() && to.isEmpty()) {
            message = "";
        } else if (from.isEmpty()) {
            message = " (to: " + to + ")";
        } else if (to.isEmpty()) {
            message = " (from: " + from + ")";
        } else {
            message = " (from: " + from + " to: " + to + ")";
        }
        return "[E]" + super.toString() + message;
    }
}
