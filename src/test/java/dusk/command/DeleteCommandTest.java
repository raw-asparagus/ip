package dusk.command;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.task.Todo;
import dusk.ui.DuskIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the DeleteCommand.
 */
public class DeleteCommandTest {

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
     * Verifies that the task is deleted when a valid index is provided.
     */
    @Test
    public void executeValidIndexDeletesTask() throws TaskListException, InputException, IOException {
        taskList.addTask(new Todo("test task"));
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));
        DeleteCommand command = new DeleteCommand(taskList, duskIO, storage, "1");

        command.execute();
        assertEquals(0, taskList.size());
        verify(storage).saveTasksAsync(taskList);
        verify(duskIO).print(
                eq("Noted. I've removed this task:"),
                eq("  [T][ ] test task"),
                eq("Now you have 0 tasks in the list.")
        );
    }

    /**
     * Verifies that an invalid index throws a TaskListException.
     */
    @Test
    public void executeInvalidIndexThrowsTaskListException() {
        DeleteCommand command = new DeleteCommand(taskList, duskIO, storage, "1");
        assertThrows(TaskListException.class, command::execute);
    }

    /**
     * Verifies that an invalid input format throws an InputException.
     */
    @Test
    public void executeInvalidInputFormatThrowsInputException() {
        DeleteCommand command = new DeleteCommand(taskList, duskIO, storage, "invalid");
        assertThrows(InputException.class, command::execute);
    }

    /**
     * Verifies that the correct task is deleted when multiple tasks exist.
     */
    @Test
    public void executeDeletesCorrectTaskWhenMultipleTasksExist()
            throws TaskListException, InputException, IOException {
        Todo task1 = new Todo("first task");
        Todo task2 = new Todo("second task");
        taskList.addTask(task1);
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));
        taskList.addTask(task2);
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));

        DeleteCommand command = new DeleteCommand(taskList, duskIO, storage, "1");
        command.execute();

        assertEquals(1, taskList.size());
        assertEquals(task2, taskList.getTask(0));
        verify(storage).saveTasksAsync(taskList);
    }
}
