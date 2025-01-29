package task;

public class Event extends Task {
    private final String from;
    private final String to;

    // Constructor
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    // Accessors
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
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
