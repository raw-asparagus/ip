package dusk.gui;

import dusk.ui.DuskResponse;
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

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A custom HBox that represents a dialog box in the chat interface.
 * It can display both user and system messages with different styles.
 */
public class DialogBox extends HBox {
    private static final Logger LOGGER = Logger.getLogger(DialogBox.class.getName());
    private static final String DIALOG_BOX_FXML = "/view/DialogBox.fxml";
    private static final String ERROR_DIALOG_BOX_FXML = "/view/ErrorDialogBox.fxml";
    private static final String SYSTEM_ERROR_STYLE =
            "; -fx-background-color: #f8d7da; -fx-text-fill: #721c24;";

    @FXML private Label dialog;
    @FXML private ImageView displayPicture;

    /**
     * Creates a dialog box with the specified text and image.
     *
     * @param text The text to display in the dialog box
     * @param img The image to display (can be null)
     * @param isError Whether this is an error message
     */
    private DialogBox(String text, Image img, boolean isError) {
        loadFxml(isError);
        configureDialog(text, img);
    }

    private DialogBox(String text, Image img) {
        this(text, img, false);
    }

    /**
     * Creates a dialog box for user messages.
     *
     * @param text The user's message
     * @param img The user's avatar image
     * @return A configured DialogBox for user messages
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /**
     * Creates a dialog box for Dusk messages with appropriate styling.
     *
     * @param text The message to display
     * @param img The Dusk avatar image
     * @param type The type of response
     * @return A configured DialogBox for Dusk messages
     */
    public static DialogBox getDuskDialog(String text, Image img, DuskResponse.ResponseType type) {
        boolean isError = type == DuskResponse.ResponseType.ERROR
                || type == DuskResponse.ResponseType.SYSTEM_ERROR;

        DialogBox db = new DialogBox(text, img, isError);

        if (type == DuskResponse.ResponseType.SYSTEM_ERROR) {
            db.dialog.setStyle(db.dialog.getStyle() + SYSTEM_ERROR_STYLE);
        }

        return db;
    }

    private void loadFxml(boolean isError) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource(
                    isError ? ERROR_DIALOG_BOX_FXML : DIALOG_BOX_FXML));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading FXML", e);
        }
    }

    private void configureDialog(String text, Image img) {
        dialog.setText(text);
        if (img != null) {
            displayPicture.setImage(img);
        } else {
            displayPicture.setManaged(false);
            displayPicture.setVisible(false);
        }
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
