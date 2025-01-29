import command.Command;
import command.Parser;
import storage.Storage;
import task.TaskList;
import ui.ConsoleIO;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
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
    private static TaskList tasks;

    // Constructor
    public Dusk() {
        // Load data
        CompletableFuture<TaskList> loadFuture = storage.loadTasksAsync();
        try {
            tasks = loadFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.log(Level.SEVERE, "Error loading tasks asynchronously.", e);
        } catch (CompletionException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    public static void app() {
        try (ConsoleIO consoleIO = new ConsoleIO(System.in, System.out)) {
            consoleIO.print(GREETING);                          // print: IOException

            String input;
            while ((input = consoleIO.readLine()) != null && !"bye".equalsIgnoreCase(input)) {  // readLine: IOException
                // Echo user input for debugging
                consoleIO.debugPrint(input);                    // print: IOException

                try {
                    Command command = Parser.parse(consoleIO, storage, tasks, input);
                    command.execute();
                } catch (Exception e) {
                    consoleIO.print("<!> " + e.getMessage());   // print: IOException
                }
            }

            consoleIO.print(BYE);                               // print: IOException
        } catch (IOException e) {
            logger.log(
                    Level.SEVERE,
                    "An error occurred while handling I/O operations using ConsoleIO.",
                    e
            );
        } finally {
            storage.shutdownExecutor();
        }
    }

    public static void main(String[] args) {
        new Dusk();
        app();
    }
}
