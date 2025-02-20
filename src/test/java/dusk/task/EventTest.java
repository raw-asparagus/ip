package dusk.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for verifying functionality of the {@link Event} task.
 */
public class EventTest {

    /**
     * Tests that constructor initializes an event with the expected fields.
     */
    @Test
    public void constructorValidInputsInitializesCorrectly() {
        LocalDateTime startTime = LocalDateTime.of(2023, 11, 1, 14, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 11, 1, 16, 0);
        Event event = new Event("Company Meeting", startTime, endTime);

        assertEquals("Company Meeting", event.getName(),
                "Event description should match constructor argument");
        assertEquals(startTime, event.getFrom(),
                "Start time should match constructor argument");
        assertEquals(endTime, event.getTo(),
                "End time should match constructor argument");
        assertFalse(event.getDone(), "A newly created event should not be marked done");
    }

    /**
     * Tests that an event falling entirely within the specified range returns {@code true}.
     */
    @Test
    public void isWithinRangeInRangeReturnsTrue() {
        LocalDateTime fromTime = LocalDateTime.of(2023, 11, 1, 10, 0);
        LocalDateTime toTime = LocalDateTime.of(2023, 11, 1, 12, 0);
        Event event = new Event("Morning Session", fromTime, toTime);

        LocalDateTime rangeStart = LocalDateTime.of(2023, 11, 1, 9, 0);
        LocalDateTime rangeEnd = LocalDateTime.of(2023, 11, 1, 13, 0);

        assertTrue(event.isWithinRange(rangeStart, rangeEnd),
                "Event times should lie within the specified range");
    }

    /**
     * Tests that an event completely outside the specified range returns {@code false}.
     */
    @Test
    public void isWithinRangeOutOfRangeReturnsFalse() {
        LocalDateTime fromTime = LocalDateTime.of(2023, 11, 1, 15, 0);
        LocalDateTime toTime = LocalDateTime.of(2023, 11, 1, 16, 0);
        Event event = new Event("Afternoon Session", fromTime, toTime);

        LocalDateTime rangeStart = LocalDateTime.of(2023, 11, 1, 9, 0);
        LocalDateTime rangeEnd = LocalDateTime.of(2023, 11, 1, 10, 0);

        assertFalse(event.isWithinRange(rangeStart, rangeEnd),
                "Event times should not lie within the specified range");
    }

    /**
     * Tests that an event on the exact specified date returns {@code true} for isOnDate().
     */
    @Test
    public void isOnDateExactDateReturnsTrue() {
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 1, 9, 30);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 1, 11, 30);
        Event event = new Event("Conference", startTime, endTime);

        LocalDateTime sameDay = LocalDateTime.of(2024, 3, 1, 0, 0);
        assertTrue(event.isOnDate(sameDay),
                "Event should be found on the same day");
    }

    /**
     * Tests that an event not on the specified date returns {@code false} for isOnDate().
     */
    @Test
    public void isOnDateDifferentDateReturnsFalse() {
        LocalDateTime startTime = LocalDateTime.of(2024, 3, 2, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 3, 2, 13, 0);
        Event event = new Event("March Gathering", startTime, endTime);

        LocalDateTime otherDay = LocalDateTime.of(2024, 3, 1, 0, 0);
        assertFalse(event.isOnDate(otherDay),
                "Event should not be found on a different day");
    }
}
