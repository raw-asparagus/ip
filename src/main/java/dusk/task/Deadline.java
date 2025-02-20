package dusk.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline, storing the date and time by which the task
 * must be completed.
 */
public class Deadline extends Task {

    /**
     * The formatter used to display the deadline date and time.
     */
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * The date and time by which this task must be completed.
     */
    private final LocalDateTime by;

    /**
     * Constructs a Deadline task with the given description and deadline date/time.
     *
     * @param description the description of the task
     * @param by          the LocalDateTime by which the task should be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Retrieves the deadline of this task.
     *
     * @return the LocalDateTime by which this task must be completed
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Checks if this deadline falls within the given start and end date/time range.
     *
     * @param start the start of the date/time range
     * @param end   the end of the date/time range
     * @return true if the deadline is within the range, false otherwise
     */
    public boolean isWithinRange(LocalDateTime start, LocalDateTime end) {
        if (by == null) {
            return false;
        }
        return !by.isBefore(start) && !by.isAfter(end);
    }

    /**
     * Checks if this deadline occurs on the specified date
     * (matching only the date portion, ignoring specific times).
     *
     * @param date the LocalDateTime whose date will be compared against this deadline
     * @return true if the deadline's date is the same as the specified date, false otherwise
     */
    public boolean isOnDate(LocalDateTime date) {
        if (by == null) {
            return false;
        }
        return by.toLocalDate().isEqual(date.toLocalDate());
    }

    /**
     * Returns a string representation of this Deadline task, including its type,
     * completion status, and deadline.
     *
     * @return a string that shows the type of the task, its status, and deadline details
     */
    @Override
    public String toString() {
        String formatted = (by == null)
                ? "no idea :p"
                : by.format(OUTPUT_FORMATTER);
        return "[D]" + super.toString() + " (by: " + formatted + ")";
    }
}
