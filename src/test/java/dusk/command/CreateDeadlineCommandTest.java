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

class CreateDeadlineCommandTest {

    private TaskList tasks;
    private ConsoleIO consoleIO;
    private Storage storage;

    @BeforeEach
    void setUp() {
        tasks = new TaskList();
        consoleIO = new ConsoleIO(System.in, System.out);
        storage = new Storage();
    }

    @Test
    void testExecute_validData() throws IOException, InputException, TaskListException {
        LocalDateTime by = LocalDateTime.now().plusDays(1);
        CreateDeadlineCommand command = new CreateDeadlineCommand(tasks, consoleIO, storage, "Finish report", by);

        command.execute();

        assertEquals(1, tasks.size(), "TaskList should have 1 task after executing a valid CreateDeadlineCommand.");
        assertFalse(tasks.getTask(0).getDone(), "Newly created task should not be marked done.");
        assertTrue(tasks.getTask(0).toString().contains("Finish report"), "Task should contain the given description in its string representation.");
    }

    @Test
    void testExecute_emptyDescription_throwsException() {
        LocalDateTime by = LocalDateTime.now().plusDays(1);
        CreateDeadlineCommand command = new CreateDeadlineCommand(tasks, consoleIO, storage, "", by);

        assertThrows(InputException.class, command::execute,
                "Executing CreateDeadlineCommand with empty description should throw an InputException.");
    }
}
