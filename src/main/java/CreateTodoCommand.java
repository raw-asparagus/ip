import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreateTodoCommand implements Command {
    private final List<Task> tasks;
    private final ConsoleIO consoleIO;
    private final Storage storage;
    private final String description;

    public CreateTodoCommand(List<Task> tasks, ConsoleIO consoleIO, Storage storage, String description) {
        this.tasks = tasks;
        this.consoleIO = consoleIO;
        this.storage = storage;
        this.description = description;
    }

    @Override
    public void execute() throws IOException, InputException {
        if (description.isEmpty()) {
            throw new InputException("A 'TODO' command must include a description.");
        }
        Todo newTask = new Todo(description);
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