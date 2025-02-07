package dusk.storage;

import dusk.task.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class StorageTest {

    private Path tempDataFile;
    private Storage storage;

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

    @AfterEach
    public void tearDown() {
        storage.shutdownExecutor();
    }

    @Test
    public void saveTasks_loadTasks_roundTripSuccess() throws Exception {
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

    @Test
    public void invalidTaskLine_parseTask_throwsStorageException() throws Exception {
        Files.writeString(tempDataFile, "X|false|InvalidLine\n");

        assertThrows(StorageException.class, () -> storage.loadTasks(),
                "Corrupted or unrecognized data should throw a StorageException");
    }

    @Test
    public void incompleteDataForEvent_throwsStorageException() throws Exception {
        Files.writeString(tempDataFile, "E|false|IncompleteEvent|2024-01-01\n");

        assertThrows(StorageException.class, () -> storage.loadTasks(),
                "Event missing required fields should cause a StorageException");
    }

    @Test
    public void parseDateTime_invalidFormat_throwsStorageException() throws Exception {
        // Create a file line with an invalid datetime format
        Files.writeString(tempDataFile, "D|true|BadDate|2024/01/01\n");

        assertThrows(StorageException.class, () -> storage.loadTasks(),
                "Invalid date format should throw a StorageException");
    }

    @Test
    public void saveTasks_ioError_throwsStorageException() {
        TaskList tasks = new TaskList();
        tasks.addTask(new Todo("Unwritable Directory Test"));

        assertDoesNotThrow(() -> storage.saveTasks(tasks),
                "If the path is writable, saving should succeed. Adjust if testing actual I/O failures.");
    }
}
