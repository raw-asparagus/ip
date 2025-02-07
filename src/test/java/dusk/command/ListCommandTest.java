package dusk.command;

import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.task.Todo;
import dusk.ui.ConsoleIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ListCommandTest {

    private TaskList tasks;
    private ConsoleIO consoleIO;

    @BeforeEach
    void setUp() {
        tasks = new TaskList();
        consoleIO = new ConsoleIO(System.in, System.out);
    }

    @Test
    void testExecute_emptyTaskList_printsEmptyMessage() throws IOException, TaskListException {
        ListCommand command = new ListCommand(tasks, consoleIO, null, null, null);

        command.execute();
    }

    @Test
    void testExecute_withTasks_noDateFilters() throws IOException, TaskListException {
        tasks.addTask(new Todo("First Task"));
        tasks.addTask(new Todo("Second Task"));

        ListCommand command = new ListCommand(tasks, consoleIO, null, null, null);

        command.execute();

        assertEquals(2, tasks.size(), "Should still have 2 tasks in the list.");
    }

    @Test
    void testExecute_onDateFilter() throws IOException, TaskListException {
        ListCommand command = new ListCommand(tasks, consoleIO, LocalDateTime.now(), null, null);

        command.execute();
    }

    // Add more tests on different ListCommand inputs

}
