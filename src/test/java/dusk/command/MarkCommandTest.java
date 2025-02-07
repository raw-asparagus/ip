package dusk.command;

import dusk.storage.Storage;
import dusk.task.*;
import dusk.ui.ConsoleIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the MarkCommand class.
 */
class MarkCommandTest {

    private TaskList tasks;
    private ConsoleIO consoleIO;
    private Storage storage;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        tasks = new TaskList();
        consoleIO = new ConsoleIO(System.in, System.out);
        storage = new Storage();
    }

    /**
     * Tests marking a task as done using a valid index.
     *
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     * @throws InputException if there is an invalid input to the command.
     * @throws MarkTaskException if marking or unmarking the task encounters an issue.
     * @throws IOException if an I/O error occurs while executing the command.
     */
    @Test
    void testExecute_validMark() throws TaskListException, InputException, MarkTaskException, IOException {
        tasks.addTask(new Todo("Sample Task"));
        MarkCommand command = new MarkCommand(tasks, consoleIO, storage, "1", true);

        command.execute();

        assertTrue(tasks.getTask(0).getDone(), "The task should be marked done.");
    }

    /**
     * Tests unmarking a previously marked task as done.
     *
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     * @throws InputException if there is an invalid input to the command.
     * @throws MarkTaskException if marking or unmarking the task encounters an issue.
     * @throws IOException if an I/O error occurs while executing the command.
     */
    @Test
    void testExecute_validUnmark() throws TaskListException, InputException, MarkTaskException, IOException {
        Task newTask = new Todo("Already done");
        newTask.markDone();
        tasks.addTask(newTask);

        MarkCommand command = new MarkCommand(tasks, consoleIO, storage, "1", false);
        command.execute();

        assertFalse(tasks.getTask(0).getDone(), "The task should be unmarked.");
    }

    /**
     * Tests that command execution with an invalid index throws an InputException.
     */
    @Test
    void testExecute_invalidIndex_throwsInputException() {
        MarkCommand command = new MarkCommand(tasks, consoleIO, storage, "abc", true);

        assertThrows(InputException.class, command::execute,
                "Invalid description for the mark command should throw InputException.");
    }
}