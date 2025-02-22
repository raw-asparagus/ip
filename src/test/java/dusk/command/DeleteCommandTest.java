package dusk.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dusk.ui.DuskIO;
import dusk.task.TaskListException;
import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.task.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;


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

    @Test
    public void executeValidIndexDeletesTask() throws TaskListException,
            InputException, IOException {
        taskList.addTask(new Todo("test task"));
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));
        DeleteCommand command = new DeleteCommand(taskList, duskIO, storage, "1");

        command.execute();
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));

        assertEquals(0, taskList.size());
        verify(storage).saveTasksAsync(taskList);
        verify(duskIO).print(
                eq("Noted. I've removed this task:"),
                anyString(),
                anyString()
        );
    }

    @Test
    public void executeInvalidIndexThrowsTaskListException() {
        DeleteCommand command = new DeleteCommand(taskList, duskIO, storage, "1");

        assertThrows(TaskListException.class, command::execute);
    }

    @Test
    public void executeInvalidInputFormatThrowsInputException() {
        DeleteCommand command = new DeleteCommand(taskList, duskIO, storage, "invalid");

        assertThrows(InputException.class, command::execute);
    }

    @Test
    public void executeDeletesCorrectTaskWhenMultipleTasksExist() throws TaskListException,
            InputException, IOException {
        Todo task1 = new Todo("first task");
        Todo task2 = new Todo("second task");
        taskList.addTask(task1);
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));
        taskList.addTask(task2);
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));

        DeleteCommand command = new DeleteCommand(taskList, duskIO, storage, "1");
        command.execute();
        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));

        assertEquals(1, taskList.size());
        assertEquals(task2, taskList.getTask(0));
        verify(storage).saveTasksAsync(taskList);
    }
}
