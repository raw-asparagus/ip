package dusk.command;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.DuskIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of the CreateTodoCommand.
 */
public class CreateTodoCommandTest {

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
     * Verifies that a valid todo task is successfully added.
     */
    @Test
    public void executeValidTodoTaskAdded() throws IOException, InputException {
        CreateTodoCommand command = new CreateTodoCommand(taskList, duskIO, storage, "Test todo");

        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));
        command.execute();

        assertEquals(1, taskList.size());
        verify(duskIO).print(eq("Got it. I've added this task:"),
                contains("  [T][ ] Test todo"),
                eq("Now you have 1 tasks in the list."));
        verify(storage).saveTasksAsync(taskList);
    }

    /**
     * Verifies that executing the command with an empty description throws an InputException.
     */
    @Test
    public void executeEmptyDescriptionThrowsInputException() {
        CreateTodoCommand command = new CreateTodoCommand(taskList, duskIO, storage, "");
        assertThrows(InputException.class, command::execute);
        assertEquals(0, taskList.size());
    }
}
