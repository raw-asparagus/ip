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

public class Main extends Application {

    /**
     * An instance of the Dusk application, responsible for handling user interactions,
     * commands, and task management. This instance is injected into the controller
     * for communication with the backend logic of the application.
     */
    private final Dusk dusk = new Dusk();

    /**
     * Logger used throughout the Dusk application.
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public Main() throws StorageException {
    }

    /**
     * Starts the main application stage by initializing and setting up the primary GUI window.
     *
     * @param stage the primary stage for the JavaFX application
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            MainWindow controller = fxmlLoader.getController();
            controller.setDusk(dusk);

            Scene scene = new Scene(ap);
            scene.getStylesheets()
                    .add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
            stage.setTitle("Dusk");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while starting the application.", e);
        }
    }
}
