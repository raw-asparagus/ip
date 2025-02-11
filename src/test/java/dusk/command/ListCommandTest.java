package dusk.command;

import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.task.Todo;
import dusk.ui.ConsoleIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the ListCommand class.
 */
class ListCommandTest {

    private TaskList taskList;
    private ConsoleIO consoleIo;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        consoleIo = new ConsoleIO(System.in, System.out);
    }

    /**
     * Tests executing a ListCommand when the TaskList is empty.
     *
     * @throws IOException if an I/O error occurs while executing the command.
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     */
    @Test
    void testExecuteEmptyTaskListPrintsEmptyMessage() throws IOException, TaskListException {
        ListCommand command = new ListCommand(taskList, consoleIo, null, null, null);
        command.execute();
        // Verifies no exceptions are thrown and execution completes
    }

    /**
     * Tests executing a ListCommand with tasks and no date filters applied.
     *
     * @throws IOException if an I/O error occurs while executing the command.
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     */
    @Test
    void testExecuteWithTasksNoDateFilters() throws IOException, TaskListException {
        taskList.addTask(new Todo("First Task"));
        taskList.addTask(new Todo("Second Task"));

        ListCommand command = new ListCommand(taskList, consoleIo, null, null, null);
        command.execute();

        assertEquals(2, taskList.size(), "Should still have 2 tasks in the list.");
    }

    /**
     * Tests executing a ListCommand with an on-date filter.
     *
     * @throws IOException if an I/O error occurs while executing the command.
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     */
    @Test
    void testExecuteOnDateFilter() throws IOException, TaskListException {
        ListCommand command = new ListCommand(taskList, consoleIo, LocalDateTime.now(), null, null);
        command.execute();
        // Verifies no exceptions are thrown and execution completes
    }
}