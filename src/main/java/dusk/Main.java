package dusk;

import java.io.IOException;

import dusk.gui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Dusk using FXML.
 */
public class Main extends Application {

    private Dusk dusk = new Dusk();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setTitle("Dusk");
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setDusk(dusk);  // inject the Dusk instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
