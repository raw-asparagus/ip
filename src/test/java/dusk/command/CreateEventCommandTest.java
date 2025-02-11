package dusk.command;

import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;
import dusk.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CreateEventCommand class.
 */
class CreateEventCommandTest {

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
     * Tests executing a valid CreateEventCommand scenario.
     *
     * @throws IOException if I/O error occurs while executing the command.
     * @throws InputException if there is an invalid input to the command.
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     */
    @Test
    void testExecuteValidData() throws IOException, InputException, TaskListException {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(2);
        CreateEventCommand command = new CreateEventCommand(
                taskList, consoleIo, storage, "Team meeting", startTime, endTime
        );

        command.execute();

        assertEquals(1, taskList.size(), "TaskList should have 1 task after executing a valid CreateEventCommand.");
        assertFalse(taskList.getTask(0).getDone(), "Newly created event should not be marked done.");
        assertTrue(
                taskList.getTask(0).toString().contains("Team meeting"),
                "Event should contain the given description in its string representation."
        );
    }

    /**
     * Tests that executing CreateEventCommand with an empty description throws an InputException.
     */
    @Test
    void testExecuteEmptyDescriptionThrowsException() {
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(1).plusHours(2);
        CreateEventCommand command = new CreateEventCommand(
                taskList, consoleIo, storage, "", startTime, endTime
        );

        assertThrows(
                InputException.class,
                command::execute,
                "Executing CreateEventCommand with empty description should throw an InputException."
        );
    }
}