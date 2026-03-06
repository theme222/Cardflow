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

/**
 * An overlay shown when the player wins a level.
 * Displays a victory message and options to go back or proceed to the next level.
 */
public class GameWinOverlay extends VBox {

    /** Label displaying the victory text. */
    private final Label gameWinText;
    /** Container for the control buttons. */
    private final HBox buttonControls;

    /**
     * Constructs a new GameWinOverlay.
     */
    public GameWinOverlay() {
        gameWinText = createTitle();
        buttonControls = createButtonControls();

        buildLayout();
        applyBackground();
    }

    /** 
     * Creates the title label.
     * 
     * @return A {@link Label}.
     */
    private Label createTitle() {
        Label label = new Label("You completed the level!");
        label.getStyleClass().addAll("text-title", "text-white");
        return label;
    }

    /** 
     * Creates the container for buttons.
     * 
     * @return An {@link HBox}.
     */
    private HBox createButtonControls() {

        Button backButton = createBackButton();
        Button nextButton = createNextButton();

        HBox box = new HBox(backButton, nextButton);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    /** 
     * Creates the back button.
     * 
     * @return A {@link Button}.
     */
    private Button createBackButton() {
        Button button = new Button("Back ≡");

        button.getStyleClass().add("button-info");

        button.setOnAction(event -> {
            AudioManager.playSoundEffect("button-click");
            ViewManager.getInstance().switchToPreviousView(TransitionType.FADE);
        });

        return button;
    }

    /** 
     * Creates the next level button.
     * 
     * @return A {@link Button}.
     */
    private Button createNextButton() {

        Button button = new Button("Next ‼");
        button.getStyleClass().add("button-primary");

        button.setOnAction(event -> handleNextLevel());

        return button;
    }

    /**
     * Handles the action of moving to the next level.
     */
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

    /** 
     * Calculates the ID for the next level.
     * 
     * @return The next level ID as a String.
     */
    private String getNextLevelId() {
        try {
            return String.valueOf(Integer.parseInt(GameLevel.getInstance().LEVELID) + 1);
        } catch (NumberFormatException e) {
            return "sandbox";
        }
    }

    /**
     * Assembles the layout of the overlay.
     */
    private void buildLayout() {
        getChildren().addAll(gameWinText, buttonControls);
        setSpacing(60);
        setAlignment(Pos.CENTER);
    }

    /**
     * Applies the semi-transparent background color.
     */
    private void applyBackground() {

        setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(0, 0, 0, 0.6),
                        CornerRadii.EMPTY,
                        Insets.EMPTY)));
    }
}
