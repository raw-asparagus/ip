package dusk.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

/**
 * Unit tests for the {@code Deadline} class.
 */
class DeadlineTest {
    private final LocalDateTime deadline = LocalDateTime.of(2024, 3, 15, 23, 59);

    /**
     * Verifies that the {@code Deadline} constructor correctly sets the description, deadline, and done status.
     */
    @Test
    void constructorValidInputsCreatesCorrectDeadline() {
        Deadline task = new Deadline("Submit report", deadline);
        assertEquals("Submit report", task.getDescription());
        assertEquals(deadline, task.getBy());
        assertFalse(task.getDone());
    }

    /**
     * Verifies that {@code isWithinRange} returns {@code true} when the deadline is within the specified range.
     */
    @Test
    void isWithinRangeDeadlineInRangeReturnsTrue() {
        Deadline task = new Deadline("Submit report", deadline);
        LocalDateTime rangeStart = LocalDateTime.of(2024, 3, 15, 0, 0);
        LocalDateTime rangeEnd = LocalDateTime.of(2024, 3, 16, 0, 0);
        assertTrue(task.isWithinRange(rangeStart, rangeEnd));
    }

    /**
     * Verifies that {@code isWithinRange} returns {@code false} for a deadline outside the range.
     */
    @Test
    void isWithinRangeDeadlineOutsideRangeReturnsFalse() {
        Deadline task = new Deadline("Submit report", deadline);
        LocalDateTime rangeStart = LocalDateTime.of(2024, 3, 16, 0, 0);
        LocalDateTime rangeEnd = LocalDateTime.of(2024, 3, 17, 0, 0);
        assertFalse(task.isWithinRange(rangeStart, rangeEnd));
    }

    /**
     * Verifies that {@code isOnDate} returns {@code true} when the deadline falls on the specified date.
     */
    @Test
    void isOnDateDeadlineOnSameDateReturnsTrue() {
        Deadline task = new Deadline("Submit report", deadline);
        LocalDateTime date = LocalDateTime.of(2024, 3, 15, 0, 0);
        assertTrue(task.isOnDate(date));
    }

    /**
     * Verifies that {@code isOnDate} returns {@code false} when the deadline is not on the specified date.
     */
    @Test
    void isOnDateDeadlineOnDifferentDateReturnsFalse() {
        Deadline task = new Deadline("Submit report", deadline);
        LocalDateTime date = LocalDateTime.of(2024, 3, 16, 0, 0);
        assertFalse(task.isOnDate(date));
    }

    /**
     * Verifies the string representation of a new {@code Deadline}.
     */
    @Test
    void toStringNewDeadlineReturnsCorrectFormat() {
        Deadline task = new Deadline("Submit report", deadline);
        String expected = "[D][ ] Submit report (by Mar 15 2024 23:59)";
        assertEquals(expected, task.toString());
    }

    /**
     * Verifies the string representation of a completed {@code Deadline}.
     */
    @Test
    void toStringCompletedDeadlineReturnsCorrectFormat() {
        Deadline task = new Deadline("Submit report", deadline);
        task.markDone();
        String expected = "[D][✗] Submit report (by Mar 15 2024 23:59)";
        assertEquals(expected, task.toString());
    }
}
