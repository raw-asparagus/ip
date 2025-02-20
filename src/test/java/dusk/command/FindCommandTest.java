package dusk.command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.task.Todo;
import dusk.ui.DuskIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Tests for the FindCommand class.
 */
class FindCommandTest {

    private TaskList tasks;
    private DuskIO duskIO;
    private ByteArrayOutputStream outContent;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        tasks = new TaskList();
        outContent = new ByteArrayOutputStream();
        duskIO = new DuskIO(System.in, new PrintStream(outContent));
    }

    /**
     * Tests executing a FindCommand when there are matching tasks.
     *
     * @throws IOException       if an I/O error occurs while executing the command.
     * @throws TaskListException if an error occurs while retrieving tasks.
     */
    @Test
    void testExecuteValidFind() throws IOException, TaskListException {
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Todo("return book"));
        tasks.addTask(new Todo("go shopping"));

        FindCommand command = new FindCommand(tasks, duskIO, "book");
        command.execute();

        String output = outContent.toString();

        // Verify output includes the header.
        assertTrue(output.contains("Here are the matching tasks in your list:"),
                "Output should include the header for matching tasks.");
        // Verify that the matching tasks are printed.
        assertTrue(output.contains("read book") && output.contains("return book"),
                "Output should include tasks that contain 'book'.");
        // Verify that non-matching tasks do not appear.
        assertFalse(output.contains("go shopping"),
                "Output should not include tasks that do not match the search keyword.");
    }

    /**
     * Tests executing a FindCommand when there are no matching tasks.
     *
     * @throws IOException       if an I/O error occurs while executing the command.
     * @throws TaskListException if an error occurs while retrieving tasks.
     */
    @Test
    void testExecuteNoMatchingTasks() throws IOException, TaskListException {
        tasks.addTask(new Todo("read book"));
        tasks.addTask(new Todo("return book"));

        FindCommand command = new FindCommand(tasks, duskIO, "exercise");
        command.execute();

        String output = outContent.toString();

        // Verify that the no matching tasks message is printed.
        assertTrue(output.contains("No matching tasks found!"),
                "Output should indicate that no matching tasks were found.");
    }
}
