package dusk.storage;

import dusk.task.Deadline;
import dusk.task.TaskList;
import dusk.task.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for verifying functionality of the Storage component.
 */
public class StorageTest {

    private Path tempDataFile;
    private Storage storage;

    /**
     * Sets up the test environment by creating a temporary data file
     * and initializing the {@link Storage} instance.
     *
     * @param tempDir The temporary directory provided by JUnit.
     * @throws IOException If an I/O error occurs while creating the file.
     */
    @BeforeEach
    public void setUp(@TempDir Path tempDir) throws IOException {
        this.tempDataFile = tempDir.resolve("data.txt");

        storage = new Storage() {
            @Override
            protected Path getDataFile() {
                return tempDataFile;
            }
        };
    }

    /**
     * Cleans up any resources used by the storage after each test.
     */
    @AfterEach
    public void tearDown() {
        storage.shutdownExecutor();
    }

    /**
     * Tests that saving and then loading tasks results in an identical list of tasks.
     *
     * @throws Exception If any exception occurs during the save or load operations.
     */
    @Test
    public void saveTasksLoadTasksRoundTripSuccess() throws Exception {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("Test Todo"));
        tasks.addTask(new Deadline("Test Deadline", LocalDateTime.of(2024, 1, 1, 12, 0)));

        storage.saveTasks(tasks);

        TaskList loadedTasks = storage.loadTasks();
        assertEquals(tasks.size(), loadedTasks.size(),
                "Loaded tasks should match the count of saved tasks");

        assertEquals("Test Todo", loadedTasks.getTask(0).getName(),
                "First task description matches after load");
        assertFalse(loadedTasks.getTask(0).getDone(),
                "First task should remain unmarked by default");

        assertEquals("Test Deadline", loadedTasks.getTask(1).getName(),
                "Second task description matches after load");
        assertFalse(loadedTasks.getTask(1).getDone(),
                "Second task should remain unmarked by default");
    }

    /**
     * Tests that an invalid task line in the data file causes a {@link StorageException}.
     *
     * @throws Exception If an I/O error occurs while writing to the file.
     */
    @Test
    public void invalidTaskLineParseTaskThrowsStorageException() throws Exception {
        Files.writeString(tempDataFile, "X|false|InvalidLine\n");

        assertThrows(StorageException.class, () -> storage.loadTasks(),
                "Corrupted or unrecognized data should throw a StorageException");
    }

    /**
     * Tests that incomplete data for an Event causes a {@link StorageException}.
     *
     * @throws Exception If an I/O error occurs while writing to the file.
     */
    @Test
    public void incompleteDataForEventThrowsStorageException() throws Exception {
        Files.writeString(tempDataFile, "E|false|IncompleteEvent|2024-01-01\n");

        assertThrows(StorageException.class, () -> storage.loadTasks(),
                "Event missing required fields should cause a StorageException");
    }

    /**
     * Tests that an invalid date/time format in the data file causes a {@link StorageException}.
     *
     * @throws Exception If an I/O error occurs while writing to the file.
     */
    @Test
    public void parseDateTimeInvalidFormatThrowsStorageException() throws Exception {
        Files.writeString(tempDataFile, "D|true|BadDate|2024/01/01\n");

        assertThrows(StorageException.class, () -> storage.loadTasks(),
                "Invalid date format should throw a StorageException");
    }

    /**
     * Verifies that attempting to save tasks does not throw an exception if no I/O errors occur.
     */
    @Test
    public void saveTasksIoErrorThrowsStorageException() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("Unwritable Directory Test"));

        assertDoesNotThrow(() -> storage.saveTasks(tasks),
                "If the path is writable, saving should succeed.");
    }
}
