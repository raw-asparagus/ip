import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreateDeadlineCommand implements Command {
    private final List<Task> tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;
    private final String by;

    public CreateDeadlineCommand(List<Task> tasks, ConsoleIO consoleIO, Storage storage, String description, String by) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
        this.by = by;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("A 'DEADLINE' command must include a description.");
        }
        Deadline newTask = new Deadline(description, by);
        tasks.add(newTask);
        consoleIO.print(
                "Got it. I've added this task:",
                "  " + newTask,
                "Now you have " + tasks.size() + " tasks in the list."
        );

        // Async save, then wait for completion
        CompletableFuture<Void> future = storage.saveTasksAsync(tasks).exceptionally(ex -> {
            try {
                consoleIO.print("<!> Error saving tasks asynchronously: " + ex.getMessage() + " at task " + newTask);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        future.join();
    }
}