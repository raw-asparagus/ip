package dusk.command;

import dusk.storage.Storage;
import dusk.task.*;
import dusk.ui.ConsoleIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MarkCommandTest {

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
    void testExecute_validMark() throws TaskListException, InputException, MarkTaskException, IOException {
        tasks.addTask(new Todo("Sample Task"));
        MarkCommand command = new MarkCommand(tasks, consoleIO, storage, "1", true);

        command.execute();

        assertTrue(tasks.getTask(0).getDone(), "The task should be marked done.");
    }

    @Test
    void testExecute_validUnmark() throws TaskListException, InputException, MarkTaskException, IOException {
        Task newTask = new Todo("Already done");
        newTask.markDone();
        tasks.addTask(newTask);

        MarkCommand command = new MarkCommand(tasks, consoleIO, storage, "1", false);
        command.execute();

        assertFalse(tasks.getTask(0).getDone(), "The task should be unmarked.");
    }

    @Test
    void testExecute_invalidIndex_throwsInputException() {
        MarkCommand command = new MarkCommand(tasks, consoleIO, storage, "abc", true);
        assertThrows(InputException.class, command::execute,
                "Invalid description for the mark command should throw InputException.");
    }
}
