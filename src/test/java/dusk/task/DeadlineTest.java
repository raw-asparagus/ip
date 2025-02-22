package dusk.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 * Test class for verifying functionality of the {@link Deadline} task.
 */
public class DeadlineTest {

    /**
     * Tests that the constructor initializes all fields correctly.
     */
    @Test
    public void constructorValidInputsInitializesCorrectly() {
        LocalDateTime byTime = LocalDateTime.of(2023, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Submit report", byTime);

        assertEquals("Submit report", deadline.getDescription(),
                "Deadline description should match constructor argument");
        assertEquals(byTime, deadline.getBy(),
                "Deadline by-time should match constructor argument");
        assertFalse(deadline.getDone(), "A newly created deadline should not be marked done");
    }

    /**
     * Tests that a deadline time within the specified range returns {@code true}.
     */
    @Test
    public void isWithinRangeTimeWithinReturnsTrue() {
        LocalDateTime byTime = LocalDateTime.of(2023, 12, 31, 12, 0);
        Deadline deadline = new Deadline("New Deadline", byTime);

        LocalDateTime startRange = LocalDateTime.of(2023, 12, 31, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2023, 12, 31, 23, 59);

        assertTrue(deadline.isWithinRange(startRange, endRange),
                "Deadline should be within the given time range");
    }

    /**
     * Tests that a deadline on a matching date returns {@code true} for isOnDate().
     */
    @Test
    public void isOnDateCorrectDateReturnsTrue() {
        LocalDateTime byTime = LocalDateTime.of(2024, 1, 1, 10, 30);
        Deadline deadline = new Deadline("Deadline for New Year", byTime);

        LocalDateTime sameDay = LocalDateTime.of(2024, 1, 1, 0, 0);
        assertTrue(deadline.isOnDate(sameDay),
                "Deadline should be recognized as occurring on the same day");
    }

    /**
     * Tests that a deadline on a non-matching date returns {@code false} for isOnDate().
     */
    @Test
    public void isOnDateDifferentDateReturnsFalse() {
        LocalDateTime byTime = LocalDateTime.of(2024, 1, 2, 10, 30);
        Deadline deadline = new Deadline("Another Deadline", byTime);

        LocalDateTime otherDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        assertFalse(deadline.isOnDate(otherDate),
                "Deadline should be recognized as not on a different day");
    }
}
