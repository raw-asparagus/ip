package dusk.task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class DeadlineTest {

    @Test
    public void constructor_validInputs_initializesCorrectly() {
        LocalDateTime byTime = LocalDateTime.of(2023, 12, 31, 23, 59);
        Deadline deadline = new Deadline("Submit report", byTime);

        assertEquals("Submit report", deadline.getName(),
                "Deadline description should match constructor argument");
        assertEquals(byTime, deadline.getBy(),
                "Deadline by-time should match constructor argument");
        assertFalse(deadline.getDone(), "A newly created deadline should not be marked done");
    }

    @Test
    public void isWithinRange_timeWithin_returnsTrue() {
        LocalDateTime byTime = LocalDateTime.of(2023, 12, 31, 12, 0);
        Deadline deadline = new Deadline("New Deadline", byTime);

        LocalDateTime startRange = LocalDateTime.of(2023, 12, 31, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2023, 12, 31, 23, 59);

        assertTrue(deadline.isWithinRange(startRange, endRange),
                "Deadline should be within the given time range");
    }

    @Test
    public void isOnDate_correctDate_returnsTrue() {
        LocalDateTime byTime = LocalDateTime.of(2024, 1, 1, 10, 30);
        Deadline deadline = new Deadline("Deadline for New Year", byTime);

        LocalDateTime sameDay = LocalDateTime.of(2024, 1, 1, 0, 0);
        assertTrue(deadline.isOnDate(sameDay),
                "Deadline should be recognized as occurring on the same day");
    }

    @Test
    public void isOnDate_differentDate_returnsFalse() {
        LocalDateTime byTime = LocalDateTime.of(2024, 1, 2, 10, 30);
        Deadline deadline = new Deadline("Another Deadline", byTime);

        LocalDateTime otherDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        assertFalse(deadline.isOnDate(otherDate),
                "Deadline should be recognized as not on a different day");
    }
}