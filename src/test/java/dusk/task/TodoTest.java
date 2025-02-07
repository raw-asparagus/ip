package dusk.task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void constructor_validDescription_initializesCorrectly() {
        Todo todo = new Todo("Buy groceries");

        assertEquals("Buy groceries", todo.getName(),
                "Todo description should match constructor argument");
        assertFalse(todo.getDone(), "A newly created todo should not be marked done");
    }

    @Test
    public void markDone_setsDoneToTrue() {
        Todo todo = new Todo("Finish homework");
        todo.markDone();
        assertTrue(todo.getDone(), "markDone() should set getDone() to true");
    }

    @Test
    public void markUndone_setsDoneToFalse() {
        Todo todo = new Todo("Finish homework");
        todo.markDone();
        todo.markUndone();
        assertFalse(todo.getDone(), "markUndone() should set getDone() to false");
    }
}
