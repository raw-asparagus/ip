package dusk.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.DuskIO;

/**
 * Tests for the CreateTodoCommand class.
 */
class CreateTodoCommandTest {

    private TaskList taskList;
    private DuskIO duskIo;
    private Storage storage;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        duskIo = new DuskIO(System.in, System.out);
        storage = new Storage();
    }

    /**
     * Tests executing a valid CreateTodoCommand scenario.
     *
     * @throws IOException       if I/O error occurs while executing the command.
     * @throws InputException    if there is an invalid input to the command.
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     */
    @Test
    void testExecuteValidData() throws IOException, InputException, TaskListException {
        CreateTodoCommand command = new CreateTodoCommand(
                taskList, duskIo, storage, "Buy groceries"
        );

        command.execute();

        assertEquals(1, taskList.size(), "TaskList should have 1 task after executing a valid CreateTodoCommand.");
        assertFalse(taskList.getTask(0).getDone(), "Newly created todo should not be marked done.");
        assertTrue(
                taskList.getTask(0).toString().contains("Buy groceries"),
                "Task should contain the given description in its string representation."
        );
    }

    /**
     * Tests that executing CreateTodoCommand with an empty description throws an InputException.
     */
    @Test
    void testExecuteEmptyDescriptionThrowsException() {
        CreateTodoCommand command = new CreateTodoCommand(taskList, duskIo, storage, "");

        assertThrows(
                InputException.class,
                command::execute,
                "Executing CreateTodoCommand with empty description should throw an InputException."
        );
    }
}
