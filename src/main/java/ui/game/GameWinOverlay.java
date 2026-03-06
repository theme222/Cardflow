package ui.game;

import application.TransitionType;
import application.ViewManager;
import application.view.GameView;
import audio.AudioManager;
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
    private final HBox buttonControls;

    public GameWinOverlay() {
        gameWinText = createTitle();
        buttonControls = createButtonControls();

        buildLayout();
        applyBackground();
    }

    private Label createTitle() {
        Label label = new Label("You completed the level!");
        label.getStyleClass().addAll("text-title", "text-white");
        return label;
    }

    private HBox createButtonControls() {

        Button backButton = createBackButton();
        Button nextButton = createNextButton();

        HBox box = new HBox(backButton, nextButton);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    private Button createBackButton() {
        Button button = new Button("Back ≡");

        button.getStyleClass().add("button-info");

        button.setOnAction(event -> {
            AudioManager.playSoundEffect("button-click");
            ViewManager.getInstance().switchToPreviousView(TransitionType.FADE);
        });

        return button;
    }

    private Button createNextButton() {

        Button button = new Button("Next ‼");
        button.getStyleClass().add("button-primary");

        button.setOnAction(event -> handleNextLevel());

        return button;
    }

    private void handleNextLevel() {

        AudioManager.playSoundEffect("button-click");

        String nextLevel = getNextLevelId();

        try {
            ViewManager.getInstance().switchViewReplace(
                    new GameView(LevelLoader.loadLevel(nextLevel)),
                    TransitionType.FADE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getNextLevelId() {
        try {
            return String.valueOf(Integer.parseInt(GameLevel.getInstance().LEVELID) + 1);
        } catch (NumberFormatException e) {
            return "sandbox";
        }
    }

    private void buildLayout() {
        getChildren().addAll(gameWinText, buttonControls);
        setSpacing(60);
        setAlignment(Pos.CENTER);
    }

    private void applyBackground() {

        setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(0, 0, 0, 0.6),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
    }
}