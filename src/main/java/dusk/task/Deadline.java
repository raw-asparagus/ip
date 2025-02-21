package dusk.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    private final LocalDateTime by;

    /**
     * Constructs a Deadline task.
     *
     * @param description the task description
     * @param by          the deadline date and time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Retrieves the deadline date and time.
     *
     * @return the deadline as a LocalDateTime
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Checks if the deadline falls within the specified range.
     *
     * @param start the range start
     * @param end   the range end
     * @return true if the deadline is within the range; false otherwise
     */
    public boolean isWithinRange(LocalDateTime start, LocalDateTime end) {
        return by != null && !by.isBefore(start) && !by.isAfter(end);
    }

    /**
     * Checks if the deadline is on the specified date.
     *
     * @param date the date to check
     * @return true if the deadline falls on the given date; false otherwise
     */
    public boolean isOnDate(LocalDateTime date) {
        return by != null && by.toLocalDate().isEqual(date.toLocalDate());
    }

    /**
     * Returns the string representation of this deadline.
     *
     * @return a formatted string with task type, status, and deadline
     */
    @Override
    public String toString() {
        String formatted = (by == null) ? "N/A" : by.format(OUTPUT_FORMATTER);
        return "[D]" + super.toString() + " (by: " + formatted + ")";
    }
}
