import dusk.Main;
import javafx.application.Application;

/**
 * Launcher class to bypass classpath issues.
 */
public class Launcher {

    /**
     * The main entry point for the application.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
