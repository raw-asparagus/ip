package dusk.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;
import dusk.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Tests for the CreateDeadlineCommand class.
 */
class CreateDeadlineCommandTest {

    private TaskList taskList;
    private ConsoleIO consoleIo;
    private Storage storage;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        consoleIo = new ConsoleIO(System.in, System.out);
        storage = new Storage();
    }

    /**
     * Tests executing a valid CreateDeadlineCommand scenario.
     *
     * @throws IOException if I/O error occurs while executing the command.
     * @throws InputException if there is an invalid input to the command.
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     */
    @Test
    void testExecuteValidData() throws IOException, InputException, TaskListException {
        LocalDateTime dueTime = LocalDateTime.now().plusDays(1);
        CreateDeadlineCommand command = new CreateDeadlineCommand(
                taskList, consoleIo, storage, "Finish report", dueTime
        );

        command.execute();

        assertEquals(1, taskList.size(), "TaskList should have 1 task after executing a valid CreateDeadlineCommand.");
        assertFalse(taskList.getTask(0).getDone(), "Newly created task should not be marked done.");
        assertTrue(
                taskList.getTask(0).toString().contains("Finish report"),
                "Task should contain the given description in its string representation."
        );
    }

    /**
     * Tests that executing CreateDeadlineCommand with an empty description throws an InputException.
     */
    @Test
    void testExecuteEmptyDescriptionThrowsException() {
        LocalDateTime dueTime = LocalDateTime.now().plusDays(1);
        CreateDeadlineCommand command = new CreateDeadlineCommand(
                taskList, consoleIo, storage, "", dueTime
        );

        assertThrows(
                InputException.class,
                command::execute,
                "Executing CreateDeadlineCommand with empty description should throw an InputException."
        );
    }
}
