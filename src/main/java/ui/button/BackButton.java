package ui.button;

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
            View destination = manager.getPreviousView();
            if (destination == null) Platform.exit();
            manager.transitionFadeView(destination);

        });

        setPadding(new Insets(10));
        setPickOnBounds(false);
        getChildren().addAll(backButton);
    }

}
