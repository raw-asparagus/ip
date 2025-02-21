package dusk.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a start and end time.
 */
public class Event extends Task {

    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");

    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Constructs an Event.
     *
     * @param description the event description
     * @param from        the start date and time
     * @param to          the end date and time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Retrieves the start date and time.
     *
     * @return the event start as a LocalDateTime
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Retrieves the end date and time.
     *
     * @return the event end as a LocalDateTime
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Checks if any part of the event falls within the specified time range.
     *
     * @param start the range start
     * @param end   the range end
     * @return true if any part of the event is within the range; false otherwise
     */
    public boolean isWithinRange(LocalDateTime start, LocalDateTime end) {
        if (from != null && to != null) {
            return (from.isAfter(start) && from.isBefore(end))
                    || (to.isAfter(start) && to.isBefore(end));
        } else if (from != null) {
            return from.isAfter(start) && from.isBefore(end);
        } else if (to != null) {
            return to.isAfter(start) && to.isBefore(end);
        }
        return false;
    }

    /**
     * Checks if the event occurs on the specified date.
     *
     * @param date the date to check
     * @return true if the event spans the specified date; false otherwise
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
     * Returns the string representation of this event.
     *
     * @return a formatted string with task type, status, and event time range
     */
    @Override
    public String toString() {
        String fromStr = (from == null) ? "" : from.format(OUTPUT_FORMATTER);
        String toStr = (to == null) ? "" : to.format(OUTPUT_FORMATTER);
        String message = "";

        if (fromStr.isEmpty() && !toStr.isEmpty()) {
            message = " (to: " + toStr + ")";
        } else if (!fromStr.isEmpty() && toStr.isEmpty()) {
            message = " (from: " + fromStr + ")";
        } else if (!fromStr.isEmpty()) {
            message = " (from: " + fromStr + " to: " + toStr + ")";
        }

        return "[E]" + super.toString() + message;
    }
}
