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
 * Represents the main user interface component for the Dusk application.
 * Contains a scrollable dialog area, a text input field, and a submit button.
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
     * Constructs a new MainWindow.
     */
    public MainWindow() {
        this.userImage = loadImage(USER_IMAGE_PATH);
        this.duskImage = loadImage(DUSK_IMAGE_PATH);
    }

    /**
     * Loads an image from the specified path.
     *
     * @param path the image path
     * @return the loaded image
     */
    private Image loadImage(String path) {
        return new Image(Objects.requireNonNull(
                this.getClass().getResourceAsStream(path)));
    }

    /**
     * Initializes the UI components.
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
     * @param d the Dusk instance
     */
    public void setDusk(Dusk d) {
        assert d != null : "Dusk instance cannot be null";
        dusk = d;
        displayDuskResponse(new DuskResponse(dusk.getGreeting(),
                DuskResponse.ResponseType.NORMAL));
    }

    /**
     * Handles the user's input and processes the response.
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
        scrollToBottom();
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
     * @param response the response to display
     */
    private void displayDuskResponse(DuskResponse response) {
        assert response != null : "Response cannot be null";
        assert dialogContainer != null : "Dialog container must be initialized";

        Image imageToUse = response.getType() == DuskResponse.ResponseType.NORMAL ? duskImage : null;
        dialogContainer.getChildren().add(
                DialogBox.getDuskDialog(response.getMessage(), imageToUse, response.getType()));
        scrollToBottom();
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

    /**
     * Scrolls the dialog view to the bottom.
     */
    private void scrollToBottom() {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}
