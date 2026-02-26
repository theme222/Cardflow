package ui.game;

import application.TransitionType;
import application.ViewManager;
import application.view.GameView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.GameLevel;
import util.LevelLoader;

import java.io.IOException;

public class GameWinOverlay extends VBox {

    private final Label gameWinText;
    private final HBox buttonControls; // Go Back, Go Next

    public GameWinOverlay() {
        gameWinText = new Label("You completed the level!");
        gameWinText.getStyleClass().addAll("text-title", "text-white");

        Button goToLevelSelectorButton = new Button("Back ≡");
        goToLevelSelectorButton.setOnAction(event -> {
            ViewManager.getInstance().switchToPreviousView(TransitionType.FADE);
        });
        goToLevelSelectorButton.getStyleClass().add("button-info");

        Button goToNextLevelButton = new Button("Next ‼");
        goToNextLevelButton.getStyleClass().add("button-primary");
        goToNextLevelButton.setOnAction(event -> {

            String nextLevel = "sandbox"; // if this next section fails
            try {
                nextLevel = String.valueOf(Integer.parseInt(GameLevel.getInstance().LEVELID) + 1);
            } catch (NumberFormatException _) { }

            try {
                ViewManager.getInstance().switchViewReplace(
                        new GameView(LevelLoader.loadLevel(nextLevel)),
                        TransitionType.FADE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        buttonControls = new HBox();
        buttonControls.setSpacing(10);
        buttonControls.setAlignment(Pos.CENTER);
        buttonControls.getChildren().addAll(goToLevelSelectorButton, goToNextLevelButton);

        getChildren().addAll(gameWinText, buttonControls);
        setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.6),  // black with 60% opacity
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        setSpacing(60);
        setAlignment(Pos.CENTER);
    }
}
