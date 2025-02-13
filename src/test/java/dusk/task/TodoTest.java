package dusk.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for verifying functionality of the {@link Todo} task.
 */
public class TodoTest {

    /**
     * Tests that the constructor initializes the description correctly.
     */
    @Test
    public void constructor_validDescription_initializesCorrectly() {
        Todo todo = new Todo("Buy groceries");
        assertEquals("Buy groceries", todo.getName(),
                "Todo description should match constructor argument");
        assertFalse(todo.getDone(), "A newly created todo should not be marked done");
    }

    /**
     * Tests that markDone() sets the "done" state to {@code true}.
     */
    @Test
    public void markDone_setsDoneToTrue() {
        Todo todo = new Todo("Finish homework");
        todo.markDone();
        assertTrue(todo.getDone(), "markDone() should set getDone() to true");
    }

    /**
     * Tests that markUndone() sets the "done" state to {@code false}.
     */
    @Test
    public void markUndone_setsDoneToFalse() {
        Todo todo = new Todo("Finish homework");
        todo.markDone();
        todo.markUndone();
        assertFalse(todo.getDone(), "markUndone() should set getDone() to false");
    }
}