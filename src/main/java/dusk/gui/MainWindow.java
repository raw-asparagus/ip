package dusk.gui;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import dusk.Dusk;
import dusk.ui.DuskResponse;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The MainWindow class represents the main user interface component for the Dusk application.
 * It consists of a scrollable dialog container to display user input and application responses,
 * a text input field for user commands, and a button to submit commands.
 */
public class MainWindow extends AnchorPane {
    private final Image userImage = new Image(Objects
            .requireNonNull(this.getClass().getResourceAsStream("/images/surtr.png")));
    private final Image duskImage = new Image(Objects
            .requireNonNull(this.getClass().getResourceAsStream("/images/dusk.png")));
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;
    private Dusk dusk;

    @FXML
    public void initialize() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
    }

    /**
     * Injects the Dusk instance
     */
    public void setDusk(Dusk d) {
        assert d != null : "Dusk instance cannot be null";
        dusk = d;
        displayDuskResponse(new DuskResponse(dusk.getGreeting(),
                DuskResponse.ResponseType.NORMAL));
    }


    /**
     * Handles the user's input and generates appropriate responses.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        assert dusk != null : "Dusk instance must be initialized before handling input";
        assert dialogContainer != null : "Dialog container must be initialized";

        dialogContainer.getChildren().add(
                DialogBox.getUserDialog(input, userImage));
        userInput.clear();

        if ("bye".equalsIgnoreCase(input)) {
            displayDuskResponse(new DuskResponse(Dusk.FAREWELL_MESSAGE,
                    DuskResponse.ResponseType.NORMAL));
            handleTermination();
        } else {
            DuskResponse response = dusk.getResponse(input);
            displayDuskResponse(response);
        }
    }


    /**
     * Displays Dusk's response in the dialog container.
     *
     * @param response the message to display
     */
    private void displayDuskResponse(DuskResponse response) {
        assert response != null : "Response cannot be null";
        assert dialogContainer != null : "Dialog container must be initialized";

        Image imageToUse = response.getType() == DuskResponse.ResponseType.NORMAL ? duskImage : null;
        dialogContainer.getChildren().add(
                DialogBox.getDuskDialog(response.getMessage(), imageToUse, response.getType()));
    }


    /**
     * Handles the termination of the application with a delay.
     */
    private void handleTermination() {
        // Disable input controls
        userInput.setDisable(true);
        sendButton.setDisable(true);

        // Schedule application termination
        CompletableFuture.delayedExecutor(5000, TimeUnit.MILLISECONDS)
                .execute(() -> Platform.runLater(() -> {
                    // Get the scene safely
                    if (this.getScene() != null && this.getScene().getWindow() != null) {
                        Stage stage = (Stage) this.getScene().getWindow();
                        stage.close();
                    }
                    Platform.exit();
                    System.exit(0);
                }));
    }

}
