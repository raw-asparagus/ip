package dusk.task;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void addTask_shouldIncreaseSize() {
        TaskList taskList = new TaskList();
        Task sampleTask = new Todo("Sample Task"); // Placeholder constructor
        assertTrue(taskList.isEmpty(), "Task list should initially be empty");

        taskList.addTask(sampleTask);
        assertEquals(1, taskList.size(), "Task list size should be 1 after adding a task");
    }

    @Test
    public void removeTask_validIndex_shouldRemoveCorrectly() throws TaskListException {
        TaskList taskList = new TaskList();
        Task sampleTask1 = new Todo("Task 1"); // Placeholder
        Task sampleTask2 = new Todo("Task 2"); // Placeholder
        taskList.addTask(sampleTask1);
        taskList.addTask(sampleTask2);
        assertEquals(2, taskList.size(), "There should be 2 tasks initially");

        Task removedTask = taskList.removeTask(0);
        assertEquals(sampleTask1, removedTask, "The removed task should match the first task added");
        assertEquals(1, taskList.size(), "Task list size should be 1 after removal");
    }

    @Test
    public void removeTask_invalidIndex_shouldThrowException() {
        TaskList taskList = new TaskList();
        // Attempt to remove from an empty list
        assertThrows(TaskListException.class, () -> taskList.removeTask(0),
                "Removing from an empty list should throw an exception");
    }

    @Test
    public void markTask_validIndex_shouldMarkTaskDone() throws TaskListException, MarkTaskException {
        TaskList taskList = new TaskList();
        Task sampleTask = new Todo("Markable Task"); // Placeholder
        taskList.addTask(sampleTask);

        taskList.markTask(0);
        assertTrue(taskList.getTask(0).getDone(), "Task at index 0 should be marked done");
    }

    @Test
    public void unmarkTask_validIndex_shouldMarkTaskUndone() throws TaskListException, MarkTaskException {
        TaskList taskList = new TaskList();
        Task sampleTask = new Todo("Unmarkable Task");
        sampleTask.markDone(); // Mark it done first
        taskList.addTask(sampleTask);

        taskList.unmarkTask(0);
        assertFalse(taskList.getTask(0).getDone(), "Task at index 0 should be marked not done");
    }

    @Test
    public void markTask_alreadyDone_shouldThrowException() {
        TaskList taskList = new TaskList();
        Task sampleTask = new Todo("Already Done Task");
        sampleTask.markDone();
        taskList.addTask(sampleTask);

        assertThrows(MarkTaskException.class, () -> taskList.markTask(0),
                "Marking a task already done should throw MarkTaskException");
    }

    @Test
    public void unmarkTask_alreadyUndone_shouldThrowException() {
        TaskList taskList = new TaskList();
        Task sampleTask = new Todo("Already Undone Task");
        // Task starts as undone by default (if that is the default behavior)
        taskList.addTask(sampleTask);

        assertThrows(MarkTaskException.class, () -> taskList.unmarkTask(0),
                "Unmarking a task already undone should throw MarkTaskException");
    }
}