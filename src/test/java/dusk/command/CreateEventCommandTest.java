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

class CreateEventCommandTest {

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
        LocalDateTime from = LocalDateTime.now().plusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1).plusHours(2);
        CreateEventCommand command = new CreateEventCommand(tasks, consoleIO, storage, "Team meeting", from, to);

        // Execute the command
        command.execute();

        assertEquals(1, tasks.size(), "TaskList should have 1 task after executing a valid CreateEventCommand.");
        assertFalse(tasks.getTask(0).getDone(), "Newly created event should not be marked done.");
        assertTrue(tasks.getTask(0).toString().contains("Team meeting"), "Event should contain the given description in its string representation.");
    }

    @Test
    void testExecute_emptyDescription_throwsException() {
        LocalDateTime from = LocalDateTime.now().plusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1).plusHours(2);
        CreateEventCommand command = new CreateEventCommand(tasks, consoleIO, storage, "", from, to);

        assertThrows(InputException.class, command::execute,
                "Executing CreateEventCommand with empty description should throw an InputException.");
    }
}
