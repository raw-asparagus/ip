package storage;

import task.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Storage {
    private static final Path DATA_FILE = Paths.get("data", "data.txt");
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    // Asynchronous wrappers
    public CompletableFuture<Void> saveTasksAsync(TaskList tasks) {
        return CompletableFuture.runAsync(() -> saveTasks(tasks), executor);
    }

    public CompletableFuture<TaskList> loadTasksAsync(TaskList tasks) {
        return CompletableFuture.supplyAsync(() -> loadTasks(tasks), executor);
    }

    // Mutators
    public void saveTasks(TaskList tasks) {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8)) {
                for (int i = 0; i < tasks.size(); i++) {
                    String line = formatTask(tasks.getTask(i));
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    private String formatTask(Task task) {
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

    // Accessors
    public TaskList loadTasks(TaskList tasks) {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            if (!Files.exists(DATA_FILE)) {
                return tasks;
            }
            try (BufferedReader reader = Files.newBufferedReader(DATA_FILE, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task parsedTask = parseTask(line);
                    if (parsedTask != null) {
                        tasks.addTask(parsedTask);
                    }
                }
            }
        } catch (IOException | StorageException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }

    private Task parseTask(String taskLine) throws StorageException {
        try {
            String[] parts = taskLine.split("\\|");
            if (parts.length < 3) {
                return null;
            }

            String taskType = parts[0];

            return getTask(parts, taskType);
        } catch (Exception e) {
            throw new StorageException("An error occurred while attempting to load data!" + e.getMessage());
        }
    }

    private static Task getTask(String[] parts, String taskType) {
        boolean isDone = Boolean.parseBoolean(parts[1]);
        String description = parts[2];

        Task task = switch (taskType) {
            case "E" -> parts.length >= 5 ? new Event(description, parts[3], parts[4]) : null;
            case "D" -> parts.length >= 4 ? new Deadline(description, parts[3]) : null;
            case "T" -> new Todo(description);
            default -> null;
        };

        if (task != null && isDone) {
            task.markDone();
        }
        return task;
    }

    public void shutdownExecutor() {
        executor.shutdown();
    }
}
