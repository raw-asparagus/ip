package dusk.storage;

import dusk.task.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Storage {
    private static final Path DATA_FILE = Paths.get("data", "data.txt");
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

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
        try {
            Files.createDirectories(DATA_FILE.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8)) {
                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.getTask(i);
                    String line = stringify(task);
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new StorageException("An error occurred while handling I/O operations using ConsoleIO.");
        } catch (IllegalArgumentException | TaskListException e) {
            throw new StorageException("Error saving tasks: " + e.getMessage());
        }
    }

    // Accessors
    private String stringify(Task task) throws IllegalArgumentException {
        String taskType;
        String taskDetails = "";
        if (task instanceof Todo) {
            taskType = "T";
        } else if (task instanceof Deadline d) {
            taskType = "D";
            taskDetails = "|" + d.getBy();
        } else if (task instanceof Event e) {
            taskType = "E";
            taskDetails = "|" + e.getFrom() + "|" + e.getTo();
        } else {
            throw new IllegalArgumentException("Unknown Task type");
        }
        return taskType + "|" + task.getDone() + "|" + task.getName() + taskDetails;
    }

    public TaskList loadTasks() throws StorageException {
        TaskList tasks = new TaskList();

        try {
            Files.createDirectories(DATA_FILE.getParent());
            if (!Files.exists(DATA_FILE)) {
                return tasks;
            }
            try (BufferedReader reader = Files.newBufferedReader(DATA_FILE, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task task = parseTask(line);
                    tasks.addTask(task);
                }
            }

            return tasks;
        } catch (IOException e) {
            throw new StorageException("An error occurred while handling I/O operations using ConsoleIO.");
        }
    }

    private Task parseTask(String taskLine) throws StorageException {
        String[] parts = taskLine.split("\\|", -1);
        if (parts.length < 3) {
            throw new StorageException("Data corrupted: less than 3 fields!");
        }

        Task task;
        try {
            task = getDataFile(parts);
        } catch (StorageException e) {
            throw new StorageException("Data corrupted: " + e.getMessage());
        }

        return task;
    }

    private Task getDataFile(String[] parts) throws StorageException {
        String taskType = parts[0];
        boolean done = Boolean.parseBoolean(parts[1]);
        String description = parts[2];

        Task task = switch (taskType) {
            case "E" -> parts.length >= 5 ? new Event(description, parts[3], parts[4]) : null;
            case "D" -> parts.length >= 4 ? new Deadline(description, parts[3]) : null;
            case "T" -> new Todo(description);
            default -> null;
        };

        if (task == null) {
            throw new StorageException("Unknown dusk.task type: " + taskType + "|" + description + "|" + done);
        }

        if (done) {
            task.markDone();
        }
        return task;
    }

    public void shutdownExecutor() {
        executor.shutdown();
    }
}
