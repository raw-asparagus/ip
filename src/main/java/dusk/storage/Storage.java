package dusk.storage;

import dusk.task.*;

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

public class Storage {
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HHmm]");
    private static final Path DATA_FILE = Paths.get("data", "data.txt");
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    protected Path getDataFile() {
        return DATA_FILE;
    }

    // Asynchronous wrappers
    public CompletableFuture<Void> saveTasksAsync(TaskList tasks) throws CompletionException {
        return CompletableFuture.runAsync(() -> {
            try {
                saveTasks(tasks);
            } catch (StorageException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public CompletableFuture<TaskList> loadTasksAsync() throws CompletionException {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return loadTasks();
            } catch (StorageException e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    // Mutators
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

    // Accessors
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

    public void shutdownExecutor() {
        executor.shutdown();
    }
}