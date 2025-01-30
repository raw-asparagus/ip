package dusk.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private final LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return by;
    }

    public boolean isWithinRange(LocalDateTime start, LocalDateTime end) {
        if (by == null) {
            return false;
        }
        return !by.isBefore(start) && !by.isAfter(end);
    }

    public boolean isOnDate(LocalDateTime date) {
        if (by == null) {
            return false;
        }
        return by.toLocalDate().isEqual(date.toLocalDate());
    }

    @Override
    public String toString() {
        String formatted = (by == null)
                ? "no idea :p"
                : by.format(OUTPUT_FORMATTER);
        return "[D]" + super.toString() + " (by: " + formatted + ")";
    }
}
