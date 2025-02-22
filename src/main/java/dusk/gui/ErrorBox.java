package dusk.gui;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * An error message box that spans the width of the chat container.
 */
public class ErrorBox extends HBox {
    private static final String ERROR_STYLE_CLASS = "error-box";

    /**
     * Constructs an ErrorBox displaying the specified error message.
     *
     * @param errorMessage the error message to display.
     */
    public ErrorBox(String errorMessage) {
        TextFlow textFlow = new TextFlow();
        Text text = new Text(errorMessage);
        text.getStyleClass().add("error-text");
        textFlow.getChildren().add(text);
        getChildren().add(textFlow);
        getStyleClass().add(ERROR_STYLE_CLASS);
        setMaxWidth(Double.MAX_VALUE);
    }
}
