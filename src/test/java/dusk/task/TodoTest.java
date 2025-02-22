package dusk.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@code Todo} class.
 */
class TodoTest {

    /**
     * Verifies that the {@code Todo} constructor correctly sets the description and the default done status.
     */
    @Test
    void constructorValidDescriptionCreatesCorrectTodo() {
        Todo todo = new Todo("Read book");
        assertEquals("Read book", todo.getDescription());
        assertFalse(todo.getDone());
    }

    /**
     * Verifies the string representation of a new {@code Todo}.
     */
    @Test
    void toStringNewTodoReturnsCorrectFormat() {
        Todo todo = new Todo("Read book");
        String expected = "[T][ ] Read book";
        assertEquals(expected, todo.toString());
    }

    /**
     * Verifies the string representation of a completed {@code Todo}.
     */
    @Test
    void toStringCompletedTodoReturnsCorrectFormat() {
        Todo todo = new Todo("Read book");
        todo.markDone();
        String expected = "[T][✗] Read book";
        assertEquals(expected, todo.toString());
    }
}
