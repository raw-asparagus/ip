import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Storage {
    private static final Path DATA_FILE = Paths.get("data", "data.txt");

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CompletableFuture<List<Task>> loadTasksAsync() {
        return CompletableFuture.supplyAsync(this::loadTasks, executor);
    }

    public CompletableFuture<Void> saveTasksAsync(List<Task> tasks) {
        return CompletableFuture.runAsync(() -> saveTasks(tasks), executor);
    }

    public List<Task> loadTasks() {
        List<Task> taskList = new ArrayList<>();
        try {
            Files.createDirectories(DATA_FILE.getParent());
            if (!Files.exists(DATA_FILE)) {
                return taskList;
            }
            try (BufferedReader reader = Files.newBufferedReader(DATA_FILE, StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Task parsedTask = parseTask(line);
                    if (parsedTask != null) {
                        taskList.add(parsedTask);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
        return taskList;
    }

    public void saveTasks(List<Task> tasks) {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    String line = formatTask(task);
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    private Task parseTask(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 3) {
                return null;
            }
            String taskType = parts[0];
            boolean done = Boolean.parseBoolean(parts[1]);
            String description = parts[2];

            switch (taskType) {
                case "T":
                    Todo todo = new Todo(description);
                    if (done) {
                        todo.markDone();
                    }
                    return todo;
                case "D":
                    if (parts.length < 4) {
                        return null;
                    }
                    String by = parts[3];
                    Deadline deadline = new Deadline(description, by);
                    if (done) {
                        deadline.markDone();
                    }
                    return deadline;
                case "E":
                    if (parts.length < 5) {
                        return null;
                    }
                    String from = parts[3];
                    String to = parts[4];
                    Event event = new Event(description, from, to);
                    if (done) {
                        event.markDone();
                    }
                    return event;
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String formatTask(Task task) {
        char type = 0;
        String details = "";
        if (task instanceof Todo) {
            type = 'T';
        } else if (task instanceof Deadline) {
            type = 'D';
            details = "|" + ((Deadline) task).getBy();
        } else if (task instanceof Event) {
            type = 'E';
            details = "|" + ((Event) task).getFrom() + "|" + ((Event) task).getTo();
        }
        return type + "|" + task.getDone() + "|" + task.getName() + details;
    }

    public void shutdownExecutor() {
        executor.shutdown();
    }
}
