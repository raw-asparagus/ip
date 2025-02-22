package dusk.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import dusk.storage.Storage;
import dusk.task.TaskList;
import dusk.ui.DuskIO;

public class CreateDeadlineCommandTest {
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
    public void executeValidDeadlineTaskAdded() throws IOException, InputException {
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);
        CreateDeadlineCommand command = new CreateDeadlineCommand(
                taskList, duskIO, storage, "Test deadline", deadline
        );

        when(storage.saveTasksAsync(taskList)).thenReturn(CompletableFuture.completedFuture(null));
        command.execute();

        assertEquals(1, taskList.size());
        verify(duskIO).print(
                eq("Got it. I've added this task:"),
                contains("  [D][ ] Test deadline (by "),
                eq("Now you have 1 tasks in the list.")
        );
        verify(storage).saveTasksAsync(taskList);
    }

    @Test
    public void executeEmptyDescriptionThrowsInputException() {
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);
        CreateDeadlineCommand command = new CreateDeadlineCommand(
                taskList, duskIO, storage, "", deadline
        );

        assertThrows(InputException.class, command::execute);
        assertEquals(0, taskList.size());
    }
}
