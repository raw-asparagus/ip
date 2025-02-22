package dusk.command;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dusk.ui.DuskIO;
import dusk.task.MarkTaskException;
import dusk.task.TaskListException;
import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.task.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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

    @Test
    public void executeValidIndexMarksTaskAsDone() throws TaskListException,
            InputException, MarkTaskException, IOException {
        // Add a task to mark
        taskList.addTask(new Todo("test task"));
        // Configure mock to return a CompletableFuture
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));

        MarkCommand command = new MarkCommand(taskList, duskIO, storage, "1", true);

        command.execute();

        assertTrue(taskList.getTask(0).getDone());
        verify(storage).saveTasksAsync(taskList);
        verify(duskIO).print(
                eq("Nice! I've marked this task as done:"),
                eq("  [T][✗] test task")
        );
    }

    @Test
    public void executeInvalidIndexThrowsTaskListException() {
        MarkCommand command = new MarkCommand(taskList, duskIO, storage, "1", true);

        assertThrows(TaskListException.class, command::execute);
    }

    @Test
    public void executeInvalidInputFormatThrowsInputException() {
        MarkCommand command = new MarkCommand(taskList, duskIO, storage, "invalid", true);

        assertThrows(InputException.class, command::execute);
    }

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
        verify(duskIO).print(
                eq("OK! I've updated this task to not done:"),
                eq("  [T][ ] test task")
        );
    }
}
