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
        if (getFrom().isEmpty() && getTo().isEmpty()) {
            message = "";
        } else if (getFrom().isEmpty()) {
            message = " (to: " + getTo() + ")";
        } else if (getTo().isEmpty()) {
            message = " (from: " + getFrom() + ")";
        } else {
            message = " (from: " + getFrom() + " to: " + getTo() + ")";
        }
        return "[E]" + super.toString() + message;
    }
}
