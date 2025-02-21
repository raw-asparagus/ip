package dusk;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import dusk.gui.MainWindow;
import dusk.storage.StorageException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The entry point of the Dusk GUI application.
 */
public class Main extends Application {

    private final Dusk dusk = new Dusk();

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Constructs a new Main instance and initializes the Dusk application.
     *
     * @throws StorageException if there is an error during initialization.
     */
    public Main() throws StorageException {
        // Initialization handled in field declaration.
    }

    /**
     * Starts the JavaFX application by setting up the primary stage with the main window.
     *
     * @param stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = fxmlLoader.load();
            MainWindow controller = fxmlLoader.getController();
            controller.setDusk(dusk);

            Scene scene = new Scene(root);
            var stylesheetUrl = getClass().getResource("/css/styles.css");
            scene.getStylesheets().add(Objects.requireNonNull(stylesheetUrl).toExternalForm());
            stage.setTitle("Dusk");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error starting the application.", e);
        }
    }
}
