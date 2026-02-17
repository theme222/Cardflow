package ui.button;

import application.TransitionType;
import application.ViewManager;
import application.view.View;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class BackButton extends VBox {

    public BackButton() {
        Button backButton = new Button("<");
        backButton.getStyleClass().add("round");
        backButton.setOnAction(e -> {

            ViewManager manager = ViewManager.getInstance();
            if (!manager.switchToPreviousView(TransitionType.FADE)) Platform.exit(); // if go back is fail just leave the game.
        });

        setPadding(new Insets(10));
        setPickOnBounds(false);
        getChildren().addAll(backButton);
    }

}
