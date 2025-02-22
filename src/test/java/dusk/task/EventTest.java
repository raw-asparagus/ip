package dusk.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

/**
 * Unit tests for the {@code Event} class.
 */
class EventTest {
    private final LocalDateTime from = LocalDateTime.of(2024, 3, 15, 14, 0);
    private final LocalDateTime to = LocalDateTime.of(2024, 3, 15, 16, 0);

    /**
     * Verifies that the {@code Event} constructor correctly sets the description, start time, end time, and done
     * status.
     */
    @Test
    void constructorValidInputsCreatesCorrectEvent() {
        Event event = new Event("Team meeting", from, to);
        assertEquals("Team meeting", event.getDescription());
        assertEquals(from, event.getFrom());
        assertEquals(to, event.getTo());
        assertFalse(event.getDone());
    }

    /**
     * Verifies that {@code isWithinRange} returns {@code true} when the event is completely within the specified range.
     */
    @Test
    void isWithinRangeEventCompletelyInRangeReturnsTrue() {
        Event event = new Event("Team meeting", from, to);
        LocalDateTime rangeStart = LocalDateTime.of(2024, 3, 15, 13, 0);
        LocalDateTime rangeEnd = LocalDateTime.of(2024, 3, 15, 17, 0);
        assertTrue(event.isWithinRange(rangeStart, rangeEnd));
    }

    /**
     * Verifies that {@code isWithinRange} returns {@code false} when the event is outside the specified range.
     */
    @Test
    void isWithinRangeEventOutsideRangeReturnsFalse() {
        Event event = new Event("Team meeting", from, to);
        LocalDateTime rangeStart = LocalDateTime.of(2024, 3, 16, 14, 0);
        LocalDateTime rangeEnd = LocalDateTime.of(2024, 3, 16, 16, 0);
        assertFalse(event.isWithinRange(rangeStart, rangeEnd));
    }

    /**
     * Verifies that {@code isOnDate} returns {@code true} when the event occurs on the specified date.
     */
    @Test
    void isOnDateEventOnSameDateReturnsTrue() {
        Event event = new Event("Team meeting", from, to);
        LocalDateTime date = LocalDateTime.of(2024, 3, 15, 0, 0);
        assertTrue(event.isOnDate(date));
    }

    /**
     * Verifies that {@code isOnDate} returns {@code false} when the event does not occur on the specified date.
     */
    @Test
    void isOnDateEventOnDifferentDateReturnsFalse() {
        Event event = new Event("Team meeting", from, to);
        LocalDateTime date = LocalDateTime.of(2024, 3, 16, 0, 0);
        assertFalse(event.isOnDate(date));
    }

    /**
     * Verifies the string representation of a new {@code Event}.
     */
    @Test
    void toStringNewEventReturnsCorrectFormat() {
        Event event = new Event("Team meeting", from, to);
        String expected = "[E][ ] Team meeting (Mar 15 2024 14:00 to Mar 15 2024 16:00)";
        assertEquals(expected, event.toString());
    }

    /**
     * Verifies the string representation of a completed {@code Event}.
     */
    @Test
    void toStringCompletedEventReturnsCorrectFormat() {
        Event event = new Event("Team meeting", from, to);
        event.markDone();
        String expected = "[E][✗] Team meeting (Mar 15 2024 14:00 to Mar 15 2024 16:00)";
        assertEquals(expected, event.toString());
    }
}
