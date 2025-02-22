package dusk.command;

import dusk.storage.Storage;
import dusk.task.MarkTaskException;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.task.Todo;
import dusk.ui.DuskIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the MarkCommand.
 */
public class MarkCommandTest {

    private TaskList taskList;
    private DuskIO duskIO;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        duskIO = mock(DuskIO.class);
        storage = mock(Storage.class);
    }

    /**
     * Verifies that executing the command with a valid index marks the task as done.
     */
    @Test
    public void executeValidIndexMarksTaskAsDone() throws TaskListException,
            InputException, MarkTaskException, IOException {
        taskList.addTask(new Todo("test task"));
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));

        MarkCommand command = new MarkCommand(taskList, duskIO, storage, "1", true);
        command.execute();

        assertTrue(taskList.getTask(0).getDone());
        verify(storage).saveTasksAsync(taskList);
        verify(duskIO).print(eq("Nice! I've marked this task as done:"), eq("  [T][✗] test task"));
    }

    /**
     * Verifies that an invalid index throws a TaskListException.
     */
    @Test
    public void executeInvalidIndexThrowsTaskListException() {
        MarkCommand command = new MarkCommand(taskList, duskIO, storage, "1", true);
        assertThrows(TaskListException.class, command::execute);
    }

    /**
     * Verifies that an invalid input format throws an InputException.
     */
    @Test
    public void executeInvalidInputFormatThrowsInputException() {
        MarkCommand command = new MarkCommand(taskList, duskIO, storage, "invalid", true);
        assertThrows(InputException.class, command::execute);
    }

    /**
     * Verifies that executing the command with a flag to unmark updates the task properly.
     */
    @Test
    public void executeUnmarkTaskUnmarksTask() throws TaskListException,
            InputException, MarkTaskException, IOException {
        Todo todo = new Todo("test task");
        todo.markDone();
        taskList.addTask(todo);
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));

        MarkCommand command = new MarkCommand(taskList, duskIO, storage, "1", false);
        command.execute();

        assertFalse(taskList.getTask(0).getDone());
        verify(storage).saveTasksAsync(taskList);
        verify(duskIO).print(eq("OK! I've updated this task to not done:"), eq("  [T][ ] test task"));
    }
}
