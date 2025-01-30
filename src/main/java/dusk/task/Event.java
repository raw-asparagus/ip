package dusk.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        String fromStr = (from == null) ? "" : from.format(OUTPUT_FORMATTER);
        String toStr = (to == null) ? "" : to.format(OUTPUT_FORMATTER);

        String message;
        if (fromStr.isEmpty() && toStr.isEmpty()) {
            message = "";
        } else if (fromStr.isEmpty()) {
            message = " (to: " + toStr + ")";
        } else if (toStr.isEmpty()) {
            message = " (from: " + fromStr + ")";
        } else {
            message = " (from: " + fromStr + " to: " + toStr + ")";
        }

        return "[E]" + super.toString() + message;
    }
}
