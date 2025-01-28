import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dusk {
    // Commons messages
    public static final String[] GREETING = {
            "Hello! I'm Dusk",
            "Anything you want me to do for you? :D"
    };
    public static final String BYE = "See ya! Hope to see you again soon! :3";

    // Commons
    private static final Logger logger = Logger.getLogger(Dusk.class.getName());
    private static final Storage storage = new Storage();
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        // Load data
        CompletableFuture<List<Task>> loadFuture = storage.loadTasksAsync();
        try {
            List<Task> loadedTasks = loadFuture.get();
            tasks.addAll(loadedTasks);
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Error loading tasks asynchronously.", e);
        }

        // App script
        try (ConsoleIO consoleIO = new ConsoleIO(System.in, System.out)) {
            consoleIO.print(GREETING);

            String input;
            while ((input = consoleIO.readLine()) != null && !"bye".equalsIgnoreCase(input)) {
                // Echo user input for debugging
                consoleIO.getWriter().write(input + "\n");
                consoleIO.getWriter().flush();

                try {
                    Command command = Parser.parse(consoleIO, storage, input, tasks);
                    command.execute();
                } catch (DuskException | InputException e) {
                    consoleIO.print("<!> " + e.getMessage());
                }
            }

            consoleIO.print(BYE);
        } catch (IOException e) {
            logger.log(
                    Level.SEVERE,
                    "An error occurred while handling I/O operations using ConsoleIO.",
                    e
            );
        } finally {
            // Cleanly shut down the executor to end the application
            storage.shutdownExecutor();
        }
    }
}
