package dusk.command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dusk.storage.Storage;
import dusk.task.MarkTaskException;
import dusk.task.Task;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.task.Todo;
import dusk.ui.DuskIO;

/**
 * Tests for the MarkCommand class.
 */
class MarkCommandTest {

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
     * Tests marking a task as done using a valid index.
     *
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     * @throws InputException    if there is an invalid input to the command.
     * @throws MarkTaskException if marking or unmarking the task encounters an issue.
     * @throws IOException       if an I/O error occurs while executing the command.
     */
    @Test
    void testExecuteValidMark() throws TaskListException, InputException, MarkTaskException, IOException {
        taskList.addTask(new Todo("Sample Task"));
        MarkCommand command = new MarkCommand(taskList, duskIo, storage, "1", true);

        command.execute();
        assertTrue(taskList.getTask(0).getDone(), "The task should be marked done.");
    }

    /**
     * Tests unmarking a previously marked task as done.
     *
     * @throws TaskListException if there is an error accessing or modifying the TaskList.
     * @throws InputException    if there is an invalid input to the command.
     * @throws MarkTaskException if marking or unmarking the task encounters an issue.
     * @throws IOException       if an I/O error occurs while executing the command.
     */
    @Test
    void testExecuteValidUnmark() throws TaskListException, InputException, MarkTaskException, IOException {
        Task newTask = new Todo("Already done");
        newTask.markDone();
        taskList.addTask(newTask);

        MarkCommand command = new MarkCommand(taskList, duskIo, storage, "1", false);
        command.execute();
        assertFalse(taskList.getTask(0).getDone(), "The task should be unmarked.");
    }

    /**
     * Tests that command execution with an invalid index throws an InputException.
     */
    @Test
    void testExecuteInvalidIndexThrowsInputException() {
        MarkCommand command = new MarkCommand(taskList, duskIo, storage, "abc", true);

        assertThrows(
                InputException.class,
                command::execute,
                "Invalid description for the mark command should throw InputException."
        );
    }
}
