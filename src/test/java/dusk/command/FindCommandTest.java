package dusk.command;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.task.Todo;
import dusk.ui.DuskIO;

/**
 * Test cases for {@link FindCommand}.
 */
public class FindCommandTest {

    private TaskList taskList;
    private DuskIO duskIO;

    /**
     * Initializes test fixtures before each test.
     */
    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        duskIO = mock(DuskIO.class);
    }

    /**
     * Tests executing the find command on an empty task list.
     */
    @Test
    public void executeEmptyTaskList() throws IOException, TaskListException {
        FindCommand command = new FindCommand(taskList, duskIO, "test");
        command.execute();

        verify(duskIO).print("No matching tasks found!");
    }

    /**
     * Tests executing the find command with an exact match.
     */
    @Test
    public void executeExactMatch() throws IOException, TaskListException {
        taskList.addTask(new Todo("Buy groceries"));
        taskList.addTask(new Todo("Do homework"));

        FindCommand command = new FindCommand(taskList, duskIO, "Buy groceries");
        command.execute();

        verify(duskIO).print(
                eq("Here are the matching tasks in your list:"),
                eq("1. [T][ ] Buy groceries")
        );
    }

    /**
     * Tests executing the find command with a partial match.
     */
    @Test
    public void executePartialMatch() throws IOException, TaskListException {
        taskList.addTask(new Todo("Buy groceries"));
        taskList.addTask(new Todo("Must buy fruits"));
        taskList.addTask(new Todo("Do homework"));

        FindCommand command = new FindCommand(taskList, duskIO, "buy");
        command.execute();

        verify(duskIO).print(
                eq("Here are the matching tasks in your list:"),
                eq("1. [T][ ] Buy groceries"),
                eq("2. [T][ ] Must buy fruits")
        );
    }

    /**
     * Tests executing the find command with combined exact and partial matches.
     */
    @Test
    public void executeCombinedExactAndPartialMatches() throws IOException, TaskListException {
        taskList.addTask(new Todo("Buy groceries"));
        taskList.addTask(new Todo("Buy fruits"));
        taskList.addTask(new Todo("Must Buy vegetables"));

        FindCommand command = new FindCommand(taskList, duskIO, "Buy");
        command.execute();

        verify(duskIO).print(
                eq("Here are the matching tasks in your list:"),
                eq("1. [T][ ] Buy groceries"),
                eq("2. [T][ ] Buy fruits"),
                eq("3. [T][ ] Must Buy vegetables")
        );
    }

    /**
     * Tests executing the find command when no tasks match.
     */
    @Test
    public void executeNoMatchingTasks() throws IOException, TaskListException {
        taskList.addTask(new Todo("Buy groceries"));
        taskList.addTask(new Todo("Do homework"));

        FindCommand command = new FindCommand(taskList, duskIO, "study");
        command.execute();

        verify(duskIO).print("No matching tasks found!");
    }

    /**
     * Tests executing the find command to ensure the search is case-sensitive.
     */
    @Test
    public void executeCaseSensitiveSearch() throws IOException, TaskListException {
        taskList.addTask(new Todo("Buy GROCERIES"));
        taskList.addTask(new Todo("buy fruits"));

        FindCommand command = new FindCommand(taskList, duskIO, "Buy");
        command.execute();

        verify(duskIO).print(
                eq("Here are the matching tasks in your list:"),
                eq("1. [T][ ] Buy GROCERIES"),
                eq("2. [T][ ] buy fruits")
        );
    }
}
