package dusk.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a start and end date/time.
 */
public class Event extends Task {

    /**
     * The formatter used to display the event date and time.
     */
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    /**
     * The start date/time of the event.
     */
    private final LocalDateTime from;

    /**
     * The end date/time of the event.
     */
    private final LocalDateTime to;

    /**
     * Constructs an Event task with a description, start date/time, and end date/time.
     *
     * @param description the description of the event
     * @param from the LocalDateTime when the event starts
     * @param to   the LocalDateTime when the event ends
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Retrieves the date/time when this event starts.
     *
     * @return the LocalDateTime representing the start of the event
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Retrieves the date/time when this event ends.
     *
     * @return the LocalDateTime representing the end of the event
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Checks whether this event intersects with the specified time range.
     *
     * @param start the start of the date/time range
     * @param end   the end of the date/time range
     * @return true if any part of the event is within the range, false otherwise
     */
    public boolean isWithinRange(LocalDateTime start, LocalDateTime end) {
        if (from != null && to != null) {
            return (from.isAfter(start) && from.isBefore(end))
                    || (to.isAfter(start) && to.isBefore(end));
        } else if (from != null) {
            return from.isAfter(start) && from.isBefore(end);
        } else if (to != null) {
            return to.isAfter(start) && to.isBefore(end);
        } else {
            return false;
        }
    }

    /**
     * Checks if this event occurs on the specified date (ignoring exact times).
     *
     * @param date the LocalDateTime whose date will be compared against this event
     * @return true if the event occurs on the specified date, false otherwise
     */
    public boolean isOnDate(LocalDateTime date) {
        if (from == null || to == null) {
            return false;
        }
        var dayStart = date.toLocalDate().atStartOfDay();
        var dayEnd = dayStart.plusDays(1).minusNanos(1);
        return !from.isAfter(dayEnd) && !to.isBefore(dayStart);
    }

    /**
     * Returns a string representation of this Event task, including its type,
     * completion status, and the event's date/time range.
     *
     * @return a string showing the event type, status, and date/time range
     */
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
