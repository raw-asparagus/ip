package dusk.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * A component that displays a circular profile picture.
 */
public class ProfilePicture extends StackPane {
    private static final double DEFAULT_SIZE = 45.0;
    private final ImageView imageView;

    /**
     * No-argument constructor required for FXML.
     * Creates a profile picture with no image and default size.
     */
    public ProfilePicture() {
        this(null, DEFAULT_SIZE);
    }

    /**
     * Creates a new profile picture with the default size.
     *
     * @param image the image to display
     */
    public ProfilePicture(Image image) {
        this(image, DEFAULT_SIZE);
    }

    /**
     * Creates a new profile picture with the specified size.
     *
     * @param image the image to display
     * @param size  the size for both the width and height of the profile picture
     */
    public ProfilePicture(Image image, double size) {

        imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        getChildren().add(imageView);
        getStyleClass().add("profile-picture");
        setPrefSize(size, size);
        setMaxSize(size, size);
        setMinSize(size, size);
    }

    /**
     * Updates the profile picture with a new image.
     *
     * @param image the new image to display
     */
    public void setImage(Image image) {
        imageView.setImage(image);
    }
}
