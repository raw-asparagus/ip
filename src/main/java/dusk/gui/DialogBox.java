package dusk.gui;

import java.io.IOException;
import java.util.Collections;

import java.util.logging.Level;
import java.util.logging.Logger;

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


/**
 * The DialogBox class represents a customizable dialog component that can
 * be used within a graphical user interface. It encapsulates a message
 * in the form of text along with an image, typically used to represent
 * dialog from either the user or the application within a chat-like system.
 * The class provides methods to create user-specific and application-specific
 * dialog boxes, with their orientations adjusted accordingly for intuitive
 * visual differentiation.
 */
public class DialogBox extends HBox {
    /**
     * Represents the text label used to display dialog messages within
     * the dialog box. This label is used to render the textual content
     * of a dialog, whether it originates from the user or the application.
     */
    @FXML
    private Label dialog;
    /**
     * Represents the ImageView used to display the profile picture or avatar
     * associated with a dialog box. Typically used to visually indicate the
     * speaker (e.g., the user or the application) in a graphical user interface.
     */
    @FXML
    private ImageView displayPicture;

    /**
     * Logger used throughout the Dusk application.
     */
    private static final Logger LOGGER = Logger.getLogger(DialogBox.class.getName());

    /**
     * Constructs a DialogBox instance with the specified text and image.
     * Loads the associated FXML layout and sets the provided text and image
     * to the appropriate UI elements in the dialog box.
     *
     * @param text the text content to display in the dialog box
     * @param img  the image to display alongside the text in the dialog box
     */
    private DialogBox(String text, Image img) {
        // Assert that text is not null as it's required for dialog
        assert text != null : "Dialog text cannot be null";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while starting the application.", e);
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Creates a {@code DialogBox} to represent the user's dialog. The dialog box contains the user's
     * message text and their associated image. The dialog orientation is flipped so that the image appears on
     * the left and the text on the right, typically to distinguish it from the application's dialog.
     *
     * @param text The message text to be displayed in the dialog box.
     * @param img  The image representing the user, displayed alongside the text.
     * @return A {@code DialogBox} object configured to represent the user's dialog.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /**
     * Creates a dialog box for displaying a message from the Dusk application, with the provided text
     * and image. The dialog box maintains the default orientation with the message and image displayed
     * in a graphical layout.
     *
     * @param text the message to be displayed in the dialog box
     * @param img  the image to be displayed alongside the message
     * @return a DialogBox containing the specified message and image
     */
    public static DialogBox getDuskDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Reverses the order of the child nodes within the DialogBox and updates their alignment.
     * This is used to flip the orientation of the DialogBox, ensuring proper positioning
     * of the text and image for differentiating between user and application responses.
     * <p>
     * The method performs the following operations:
     * 1. Retrieves the list of child nodes currently present in the DialogBox.
     * 2. Reverses the order of these child nodes.
     * 3. Updates the DialogBox's children to reflect the reversed order.
     * 4. Sets the alignment of the DialogBox to top-left (Pos.TOP_LEFT).
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }
}
