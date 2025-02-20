package dusk.gui;

import dusk.Dusk;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Dusk dusk;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/surtr.png"));
    private Image duskImage = new Image(this.getClass().getResourceAsStream("/images/dusk.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Dusk instance */
    public void setDusk(Dusk d) {
        dusk = d;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Dusk's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = dusk.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDuskDialog(response, duskImage)
        );
        userInput.clear();
    }
}
