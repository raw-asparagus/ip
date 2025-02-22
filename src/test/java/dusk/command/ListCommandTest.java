package dusk.command;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.io.IOException;

import dusk.task.Deadline;
import dusk.task.Event;
import dusk.task.TaskList;
import dusk.task.TaskListException;
import dusk.task.Todo;
import dusk.ui.DuskIO;

/**
 * Test cases for {@link ListCommand}.
 */
public class ListCommandTest {

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
     * Tests executing the list command with an empty task list.
     */
    @Test
    public void executeEmptyTaskList() throws IOException, TaskListException, InputException {
        ListCommand command = new ListCommand(taskList, duskIO, null, null, null);
        command.execute();

        verify(duskIO).print("Task list is empty!");
    }

    /**
     * Tests executing the list command with tasks present.
     */
    @Test
    public void executeListAllTasks() throws IOException, TaskListException, InputException {
        taskList.addTask(new Todo("test task 1"));
        taskList.addTask(new Todo("test task 2"));

        ListCommand command = new ListCommand(taskList, duskIO, null, null, null);
        command.execute();

        verify(duskIO).print(
                eq("Here are all the tasks:"),
                eq("1. [T][ ] test task 1"),
                eq("2. [T][ ] test task 2")
        );
    }

    /**
     * Tests executing the list command to retrieve tasks within a specified date range.
     */
    @Test
    public void executeListTasksWithinDateRange() throws IOException, TaskListException, InputException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        LocalDateTime dayAfterTomorrow = tomorrow.plusDays(1);
        LocalDateTime nextWeek = now.plusWeeks(1);

        taskList.addTask(new Event("event 1", now, now.plusHours(2)));
        taskList.addTask(new Deadline("deadline 1", tomorrow));
        taskList.addTask(new Event("event 2", nextWeek, nextWeek.plusHours(1)));
        taskList.addTask(new Deadline("deadline 2", nextWeek));

        ListCommand command = new ListCommand(taskList, duskIO, null, now, dayAfterTomorrow);
        command.execute();

        verify(duskIO).print(
                contains("Here are the tasks between "),
                contains("1. [E][ ] event 1 ("),
                contains("2. [D][ ] deadline 1 (by ")
        );
    }

    /**
     * Tests executing the list command when no tasks match the specified date range.
     */
    @Test
    public void executeListTasksNoMatchInDateRange() throws IOException, TaskListException, InputException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusWeeks(1);

        taskList.addTask(new Event("future event", nextWeek, nextWeek.plusHours(1)));

        ListCommand command = new ListCommand(taskList, duskIO, null, now, now.plusDays(1));
        command.execute();

        verify(duskIO).print("No matching tasks found!");
    }

    /**
     * Tests executing the list command with a specific date filter.
     */
    @Test
    public void executeListTasksWithSpecificDateOnly() throws IOException, TaskListException, InputException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        LocalDateTime dayAfterTomorrow = tomorrow.plusDays(1);
        LocalDateTime nextWeek = now.plusWeeks(1);

        taskList.addTask(new Event("past event", now.minusDays(1), now));
        taskList.addTask(new Event("event 1", now, dayAfterTomorrow.plusHours(2)));
        taskList.addTask(new Event("event 2", nextWeek, nextWeek.plusHours(1)));

        ListCommand command = new ListCommand(taskList, duskIO, now, null, null);
        command.execute();

        verify(duskIO).print(
                contains("Here are the tasks on "),
                contains("1. [E][ ] past event ("),
                contains("2. [E][ ] event 1 (")
        );
    }
}
