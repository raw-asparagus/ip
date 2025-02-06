package dusk.command;

import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.ui.ConsoleIO;
import dusk.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CreateTodoCommandTest {

    private TaskList tasks;
    private ConsoleIO consoleIO;
    private Storage storage;

    @BeforeEach
    void setUp() {
        // Replace these placeholder initializations with your actual or mocked instances
        tasks = new TaskList();
        consoleIO = new ConsoleIO(System.in, System.out);
        storage = new Storage();
    }

    @Test
    void testExecute_validData() throws IOException, InputException, TaskListException {
        CreateTodoCommand command = new CreateTodoCommand(tasks, consoleIO, storage, "Buy groceries");

        // Execute the command
        command.execute();

        assertEquals(1, tasks.size(), "TaskList should have 1 task after executing a valid CreateTodoCommand.");
        assertFalse(tasks.getTask(0).getDone(), "Newly created todo should not be marked done.");
        assertTrue(tasks.getTask(0).toString().contains("Buy groceries"), "Task should contain the given description in its string representation.");
    }

    @Test
    void testExecute_emptyDescription_throwsException() {
        CreateTodoCommand command = new CreateTodoCommand(tasks, consoleIO, storage, "");

        assertThrows(InputException.class, command::execute,
                "Executing CreateTodoCommand with empty description should throw an InputException.");
    }
}
