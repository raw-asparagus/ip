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
     * Constructs a ProfilePicture with no image and default size.
     */
    public ProfilePicture() {
        this(null, DEFAULT_SIZE);
    }

    /**
     * Constructs a ProfilePicture with the specified image and default size.
     *
     * @param image the image to display.
     */
    public ProfilePicture(Image image) {
        this(image, DEFAULT_SIZE);
    }

    /**
     * Constructs a ProfilePicture with the specified image and size.
     *
     * @param image the image to display.
     * @param size  the size for the profile picture.
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
     * @param image the new image to display.
     */
    public void setImage(Image image) {
        imageView.setImage(image);
    }
}
