package dusk.gui;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import dusk.Dusk;
import dusk.ui.DuskResponse;
import dusk.ui.DuskResponseType;
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
 * The primary user interface component that handles user interactions for the Dusk application.
 */
public class MainWindow extends AnchorPane {
    private static final String USER_IMAGE_PATH = "/images/surtr.png";
    private static final String DUSK_IMAGE_PATH = "/images/dusk.png";

    private final Image userImage;
    private final Image duskImage;
    private Dusk dusk;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    /**
     * Constructs a MainWindow.
     */
    public MainWindow() {
        // Constructor implementation.
        userImage = loadImage(USER_IMAGE_PATH);
        duskImage = loadImage(DUSK_IMAGE_PATH);
    }

    /**
     * Loads an image from the specified path.
     *
     * @param path the image path.
     * @return the loaded image.
     */
    private Image loadImage(String path) {
        return new Image(Objects.requireNonNull(
                this.getClass().getResourceAsStream(path)));
    }

    /**
     * Initializes the user interface components.
     */
    @FXML
    public void initialize() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
    }

    /**
     * Injects the Dusk instance.
     *
     * @param d the Dusk instance.
     */
    public void setDusk(Dusk d) {
        assert d != null : "Dusk instance cannot be null";
        dusk = d;
        displayDuskResponse(new DuskResponse(dusk.getGreeting(),
                DuskResponseType.NORMAL));
    }

    /**
     * Processes the user input and handles the corresponding response.
     */
    @FXML
    private void handleUserInput() {
        String trimmedInput = userInput.getText().trim();
        if (trimmedInput.isEmpty()) {
            clearUserInput();
        } else if (isExitCommand(trimmedInput)) {
            processExitCommand();
        } else {
            processUserMessage(trimmedInput);
        }
    }

    /**
     * Determines whether the provided input is an exit command.
     *
     * @param input the user input.
     * @return {@code true} if the input is an exit command, otherwise {@code false}.
     */
    private boolean isExitCommand(String input) {
        return "bye".equalsIgnoreCase(input);
    }

    /**
     * Processes an exit command.
     */
    private void processExitCommand() {
        displayDuskResponse(new DuskResponse(Dusk.FAREWELL_MESSAGE, DuskResponseType.NORMAL));
        handleTermination();
    }

    /**
     * Displays Dusk's response in the dialog container.
     *
     * @param response the response to display.
     */
    private void displayDuskResponse(DuskResponse response) {
        if (response.getType() == DuskResponseType.ERROR) {
            displayError(response.getMessage());
        } else {
            DialogBox duskDialog = DialogBox.getDuskDialog(response.getMessage(), duskImage, response.getType());
            // Add the dialog and wait for it to be properly added
            dialogContainer.getChildren().add(duskDialog);

            // Wait for layout to complete before scrolling
            duskDialog.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {  // Layout is complete
                    scrollToBottom();
                }
            });
        }
    }

    /**
     * Processes a user message.
     *
     * @param message the user message.
     */
    private void processUserMessage(String message) {
        DialogBox userDialog = DialogBox.getUserDialog(message, userImage);
        dialogContainer.getChildren().add(userDialog);

        if (dusk != null) {
            CompletableFuture.supplyAsync(() -> dusk.getResponse(message))
                    .thenAccept(response -> Platform.runLater(() -> displayDuskResponse(response)))
                    .exceptionally(error -> {
                        Platform.runLater(() -> displayError("An unexpected error occurred: " + error.getMessage()));
                        return null;
                    });
        }

        userDialog.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {  // Layout is complete
                scrollToBottom();
            }
        });
        clearUserInput();
    }

    /**
     * Clears the user input field.
     */
    private void clearUserInput() {
        userInput.clear();
    }

    /**
     * Displays an error message in the dialog container.
     *
     * @param errorMessage the error message to display.
     */
    private void displayError(String errorMessage) {
        ErrorBox errorBox = new ErrorBox(errorMessage);
        dialogContainer.getChildren().add(errorBox);

        errorBox.needsLayoutProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {  // Layout is complete
                scrollToBottom();
            }
        });
    }

    /**
     * Terminates the application after a delay.
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

    /**
     * Scrolls the dialog container to the bottom.
     */
    private void scrollToBottom() {
        // Request layout pass to ensure all elements are properly sized
        dialogContainer.requestLayout();

        // Use Platform.runLater to ensure scroll happens after layout
        Platform.runLater(() -> {
            // Wait for next layout pass
            dialogContainer.layout();

            // Scroll to bottom
            scrollPane.setVvalue(1.0);

            // Double-check scroll in case of dynamic content loading
            Platform.runLater(() -> scrollPane.setVvalue(1.0));
        });
    }
}
