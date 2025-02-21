package dusk.gui;

import java.io.IOException;
import java.util.Collections;

import java.util.logging.Level;
import java.util.logging.Logger;

import dusk.DuskResponse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private static final Logger LOGGER = Logger.getLogger(DialogBox.class.getName());
    private static final String DIALOG_BOX_FXML = "/view/DialogBox.fxml";
    private static final String ERROR_DIALOG_BOX_FXML = "/view/ErrorDialogBox.fxml";

    private DialogBox(String text, Image img, boolean isError) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource(
                    isError ? ERROR_DIALOG_BOX_FXML : DIALOG_BOX_FXML));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading FXML", e);
        }

        dialog.setText(text);
        if (img != null) {
            displayPicture.setImage(img);
        } else {
            // Hide the ImageView if no image is provided
            displayPicture.setManaged(false);
            displayPicture.setVisible(false);
        }
    }

    private DialogBox(String text, Image img) {
        this(text, img, false);
    }

    /**
     * Creates a dialog box for user messages.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /**
     * Creates a dialog box for Dusk messages with appropriate styling based on response type.
     */
    public static DialogBox getDuskDialog(String text, Image img, DuskResponse.ResponseType type) {
        boolean isError = type == DuskResponse.ResponseType.ERROR
                || type == DuskResponse.ResponseType.SYSTEM_ERROR;

        DialogBox db = new DialogBox(text, img, isError);

        // Add additional styling for system errors
        if (type == DuskResponse.ResponseType.SYSTEM_ERROR) {
            db.dialog.setStyle(db.dialog.getStyle() +
                    "; -fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
        }

        return db;
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }
}
