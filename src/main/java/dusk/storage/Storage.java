package dusk.storage;

import dusk.task.Task;
import dusk.task.Deadline;
import dusk.task.Event;
import dusk.task.Todo;
import dusk.task.TaskList;
import dusk.task.TaskListException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages reading and writing Task data to a persistent storage file, as well
 * as providing asynchronous operations for loading and saving.
 */
public class Storage {

    /**
     * Formatter to handle date/time information stored in the data file.
     */
    private static final DateTimeFormatter STORAGE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd[ HHmm]");

    /**
     * Path to the file used for persistent storage of tasks.
     */
    private static final Path DATA_FILE = Paths.get("data", "data.txt");

    /**
     * Single-threaded executor service for asynchronous operations.
     */
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    /**
     * Retrieves the path to the data file.
     *
     * @return the path of the data file
     */
    protected Path getDataFile() {
        return DATA_FILE;
    }

    /**
     * Asynchronously saves all tasks in the specified TaskList to the storage file.
     *
     * @param tasks the TaskList containing tasks to be saved
     * @return a CompletableFuture representing the completion of the save operation
     * @throws CompletionException if a StorageException occurs during the save process
     */
    public CompletableFuture<Void> saveTasksAsync(TaskList tasks) throws CompletionException {
        return CompletableFuture.runAsync(() -> {
            try {
                saveTasks(tasks);
            } catch (StorageException e) {
                throw new CompletionException(e);
            }
        }, EXECUTOR_SERVICE);
    }

    /**
     * Asynchronously loads all tasks from the storage file into a TaskList.
     *
     * @return a CompletableFuture resolving to the loaded TaskList
     * @throws CompletionException if a StorageException occurs during the load process
     */
    public CompletableFuture<TaskList> loadTasksAsync() throws CompletionException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return loadTasks();
            } catch (StorageException e) {
                throw new CompletionException(e);
            }
        }, EXECUTOR_SERVICE);
    }

    /**
     * Saves all tasks from the given TaskList to the data file.
     *
     * @param tasks the TaskList containing tasks to be saved
     * @throws StorageException if an I/O error occurs or tasks cannot be saved properly
     */
    public void saveTasks(TaskList tasks) throws StorageException {
        Path dataFile = getDataFile();
        try {
            Files.createDirectories(dataFile.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(dataFile, StandardCharsets.UTF_8)) {
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.getTask(i);
                    String line = stringify(task);
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new StorageException("An error occurred while handling I/O operations.");
        } catch (IllegalArgumentException | TaskListException e) {
            throw new StorageException("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Loads all tasks from the data file into a new TaskList and returns it.
     *
     * @return a TaskList containing the loaded Task objects
     * @throws StorageException if an I/O error occurs during load operations
     */
    public TaskList loadTasks() throws StorageException {
        TaskList tasks = new TaskList();
        Path dataFile = getDataFile();
        try {
            Files.createDirectories(dataFile.getParent());
            if (!Files.exists(dataFile)) {
                return tasks;
            }
            try (BufferedReader reader = Files.newBufferedReader(dataFile, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task task = parseTask(line);
                    tasks.addTask(task);
                }
            }
            return tasks;
        } catch (IOException e) {
            throw new StorageException("An error occurred while handling I/O operations.");
        }
    }

    /**
     * Shuts down the executor service used for asynchronous operations.
     */
    public void shutdownExecutor() {
        EXECUTOR_SERVICE.shutdown();
    }

    /**
     * Converts a Task to its string representation for storage.
     *
     * @param task the Task to be converted
     * @return the string form of the Task suitable for storage
     * @throws IllegalArgumentException if the Task type is unrecognized
     */
    private String stringify(Task task) throws IllegalArgumentException {
        String taskType;
        String taskDetails = "";
        boolean isDone = task.getDone();
        String name = task.getName();

        if (task instanceof Todo) {
            taskType = "T";
        } else if (task instanceof Deadline d) {
            taskType = "D";
            String byString = (d.getBy() == null) ? "" : d.getBy().format(STORAGE_FORMATTER);
            taskDetails = "|" + byString;
        } else if (task instanceof Event e) {
            taskType = "E";
            String fromString = (e.getFrom() == null) ? "" : e.getFrom().format(STORAGE_FORMATTER);
            String toString = (e.getTo() == null) ? "" : e.getTo().format(STORAGE_FORMATTER);
            taskDetails = "|" + fromString + "|" + toString;
        } else {
            throw new IllegalArgumentException("Unknown Task type");
        }

        return taskType + "|" + isDone + "|" + name + taskDetails;
    }

    /**
     * Parses a single line of stored data into a Task.
     *
     * @param taskLine the string representing the Task's data
     * @return the Task parsed from the line data
     * @throws StorageException if the data is corrupted or invalid
     */
    private Task parseTask(String taskLine) throws StorageException {
        String[] parts = taskLine.split("\\|", -1);
        if (parts.length < 3) {
            throw new StorageException("Data corrupted: < 3 fields!");
        }

        Task task;
        try {
            task = parseTaskParts(parts);
        } catch (StorageException e) {
            throw new StorageException("Data corrupted: " + e.getMessage());
        }

        return task;
    }

    /**
     * Interprets the split string array as components of a Task object.
     *
     * @param parts the array of data fields
     * @return a Task object based on the data
     * @throws StorageException if the data is missing required fields or has an unknown type
     */
    private Task parseTaskParts(String[] parts) throws StorageException {
        String taskType = parts[0];
        boolean done = Boolean.parseBoolean(parts[1]);
        String description = parts[2];

        Task task = switch (taskType) {
            case "E" -> {
                if (parts.length < 5) {
                    throw new StorageException("Event data missing fields: " + String.join("|", parts));
                }
                LocalDateTime from = parseDateTime(parts[3]);
                LocalDateTime to = parseDateTime(parts[4]);
                yield new Event(description, from, to);
            }
            case "D" -> {
                if (parts.length < 4) {
                    throw new StorageException("Deadline data missing fields: " + String.join("|", parts));
                }
                LocalDateTime by = parseDateTime(parts[3]);
                yield new Deadline(description, by);
            }
            case "T" -> new Todo(description);
            default -> null;
        };

        if (task == null) {
            throw new StorageException("Unknown task type: " + taskType + "|" + description + "|" + done);
        }

        if (done) {
            task.markDone();
        }
        return task;
    }

    /**
     * Parses a string into a LocalDateTime based on the designated formatter.
     *
     * @param dateStr the string representing the date/time
     * @return a LocalDateTime if valid, or {@code null} if blank
     * @throws StorageException if the date/time string is invalid
     */
    private LocalDateTime parseDateTime(String dateStr) throws StorageException {
        if (dateStr.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, STORAGE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new StorageException("Invalid date format: \"" + dateStr + "\"");
        }
    }
}
