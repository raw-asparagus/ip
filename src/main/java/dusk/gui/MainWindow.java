package dusk.gui;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import dusk.Dusk;
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
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Dusk instance
     */
    public void setDusk(Dusk d) {
        assert d != null : "Dusk instance cannot be null";

        dusk = d;

        // Display greeting messages when window initializes
        displayDuskResponse(dusk.getGreeting());
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

        // Assert that dusk instance has been initialized
        assert dusk != null : "Dusk instance must be initialized before handling input";

        // Assert that dialog container exists
        assert dialogContainer != null : "Dialog container must be initialized";


        // Display user input
        dialogContainer.getChildren().add(
                DialogBox.getUserDialog(input, userImage));
        userInput.clear();

        // Generate and display Dusk's response
        if ("bye".equalsIgnoreCase(input)) {
            displayDuskResponse(Dusk.FAREWELL_MESSAGE);
            handleTermination();
        } else {
            String response = dusk.getResponse(input);
            displayDuskResponse(response);
        }
    }

    /**
     * Displays Dusk's response in the dialog container.
     *
     * @param response the message to display
     */
    private void displayDuskResponse(String response) {
        // Assert response is not null as it's required for display
        assert response != null : "Response cannot be null";
        assert dialogContainer != null : "Dialog container must be initialized";

        dialogContainer.getChildren().add(
                DialogBox.getDuskDialog(response, duskImage));
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
