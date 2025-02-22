package dusk.gui;

import dusk.ui.DuskResponseType;
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
 * A custom dialog box for the chat interface.
 * Displays either user or system messages with appropriate styling.
 */
public class DialogBox extends HBox {
    private static final Logger LOGGER = Logger.getLogger(DialogBox.class.getName());
    private static final String DIALOG_BOX_FXML = "/view/DialogBox.fxml";
    private static final String ERROR_DIALOG_BOX_FXML = "/view/ErrorDialogBox.fxml";
    private static final String SYSTEM_ERROR_STYLE = "; -fx-background-color: #f8d7da; -fx-text-fill: #721c24;";

    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Private constructor to create a dialog box.
     *
     * @param text    the text to display
     * @param img     the image (can be null)
     * @param isError {@code true} if this dialog represents an error message
     */
    private DialogBox(String text, Image img, boolean isError) {
        loadFxml(isError);
        configureDialog(text, img);
    }

    /**
     * Private constructor for non-error messages.
     *
     * @param text the text to display
     * @param img  the image (can be null)
     */
    private DialogBox(String text, Image img) {
        this(text, img, false);
    }

    /**
     * Creates a dialog box for user messages.
     *
     * @param text the user's message
     * @param img  the user's avatar image
     * @return a dialog box configured for user messages
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /**
     * Creates a dialog box for Dusk messages with styling based on response type.
     *
     * @param text the message to display
     * @param img  the Dusk avatar image
     * @param type the type of response
     * @return a dialog box configured for Dusk messages
     */
    public static DialogBox getDuskDialog(String text, Image img, DuskResponseType type) {
        boolean isError = type == DuskResponseType.ERROR || type == DuskResponseType.SYSTEM_ERROR;
        DialogBox db = new DialogBox(text, img, isError);

        if (type == DuskResponseType.SYSTEM_ERROR) {
            db.dialog.setStyle(db.dialog.getStyle() + SYSTEM_ERROR_STYLE);
        }

        return db;
    }

    /**
     * Loads the appropriate FXML layout.
     *
     * @param isError {@code true} to load the error-specific layout
     */
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

    /**
     * Configures the dialog with the specified text and image.
     *
     * @param text the text to display
     * @param img  the image to display; if null the image view is hidden
     */
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
     * Flips the dialog box so that the image appears on the left.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        Collections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
    }
}
