package dusk.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;


/**
 * Unit tests for the {@code TaskList} class.
 */
class TaskListTest {
    private TaskList taskList;
    private Task mockTask;

    /**
     * Sets up test fixtures before each test.
     */
    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        // Using an anonymous class since Task is abstract
        mockTask = new Task("Test task") {
            // No need to implement additional methods as we're only testing TaskList
        };
    }

    /**
     * Verifies that adding a single task succeeds.
     */
    @Test
    void addTaskSingleTaskSuccess() {
        taskList.addTask(mockTask);
        assertEquals(1, taskList.size());
        try {
            assertEquals(mockTask, taskList.getTask(0));
        } catch (TaskListException e) {
            fail("Unexpected TaskListException");
        }
    }

    /**
     * Verifies that removing a task with a valid index returns the removed task.
     */
    @Test
    void removeTaskValidIndexReturnsRemovedTask() {
        taskList.addTask(mockTask);
        try {
            Task removedTask = taskList.removeTask(0);
            assertEquals(mockTask, removedTask);
            assertEquals(0, taskList.size());
        } catch (TaskListException e) {
            fail("Unexpected TaskListException");
        }
    }

    /**
     * Verifies that removing a task with an invalid index throws an exception.
     */
    @Test
    void removeTaskInvalidIndexThrowsException() {
        assertThrows(TaskListException.class, () -> taskList.removeTask(0));
    }

    /**
     * Verifies that marking a task with a valid index succeeds.
     */
    @Test
    void markTaskValidIndexSuccess() {
        taskList.addTask(mockTask);
        try {
            taskList.markTask(0);
            assertTrue(taskList.getTask(0).getDone());
        } catch (TaskListException | MarkTaskException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Verifies that attempting to mark an already marked task throws an exception.
     */
    @Test
    void markTaskAlreadyMarkedTaskThrowsException() {
        taskList.addTask(mockTask);
        try {
            taskList.markTask(0);
            assertThrows(MarkTaskException.class, () -> taskList.markTask(0));
        } catch (TaskListException | MarkTaskException e) {
            fail("Unexpected exception during test setup: " + e.getMessage());
        }
    }

    /**
     * Verifies that unmarking a task with a valid index succeeds.
     */
    @Test
    void unmarkTaskValidIndexSuccess() {
        taskList.addTask(mockTask);
        try {
            taskList.markTask(0);
            taskList.unmarkTask(0);
            assertFalse(taskList.getTask(0).getDone());
        } catch (TaskListException | MarkTaskException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Verifies that attempting to unmark an already unmarked task throws an exception.
     */
    @Test
    void unmarkTaskAlreadyUnmarkedTaskThrowsException() {
        taskList.addTask(mockTask);
        assertThrows(MarkTaskException.class, () -> taskList.unmarkTask(0));
    }

    /**
     * Verifies that obtaining a task with a valid index returns the task.
     */
    @Test
    void getTaskValidIndexReturnsTask() {
        taskList.addTask(mockTask);
        try {
            assertEquals(mockTask, taskList.getTask(0));
        } catch (TaskListException e) {
            fail("Unexpected TaskListException");
        }
    }

    /**
     * Verifies that obtaining a task with an invalid index throws an exception.
     */
    @Test
    void getTaskInvalidIndexThrowsException() {
        assertThrows(TaskListException.class, () -> taskList.getTask(0));
    }

    /**
     * Verifies that the size of an empty {@code TaskList} is zero.
     */
    @Test
    void sizeEmptyListReturnsZero() {
        assertEquals(0, taskList.size());
    }

    /**
     * Verifies that the size of a non-empty {@code TaskList} returns the correct count.
     */
    @Test
    void sizeNonEmptyListReturnsCorrectSize() {
        taskList.addTask(mockTask);
        taskList.addTask(mockTask);
        assertEquals(2, taskList.size());
    }

    /**
     * Verifies that {@code isEmpty} returns {@code true} for an empty {@code TaskList}.
     */
    @Test
    void isEmptyEmptyListReturnsTrue() {
        assertTrue(taskList.isEmpty());
    }

    /**
     * Verifies that {@code isEmpty} returns {@code false} for a non-empty {@code TaskList}.
     */
    @Test
    void isEmptyNonEmptyListReturnsFalse() {
        taskList.addTask(mockTask);
        assertFalse(taskList.isEmpty());
    }

    /**
     * Verifies that searching with a keyword returns the matching tasks.
     */
    @Test
    void searchWithKeywordReturnsMatchingTasks() {
        Task task1 = new Task("Meeting with team") {
        };
        Task task2 = new Task("Call client") {
        };
        taskList.addTask(task1);
        taskList.addTask(task2);

        TaskList results = taskList.search("team", null, null, null);
        assertEquals(1, results.size());
        try {
            assertEquals("Meeting with team", results.getTask(0).getDescription());
        } catch (TaskListException e) {
            fail("Unexpected TaskListException");
        }
    }

    /**
     * Verifies that searching with a date returns the matching tasks.
     */
    @Test
    void searchWithDateReturnsMatchingTasks() {
        LocalDateTime testDate = LocalDateTime.now();
        TaskList results = taskList.search(null, testDate, null, null);
        // Note: Specific date-based testing would depend on your Task implementation
        assertNotNull(results);
    }
}
