package application.view;

import application.TransitionType;
import application.ViewManager;
import audio.AudioManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import ui.button.BackButton;
import util.LevelLoader;

import java.io.IOException;

/**
 * The {@code LevelSelectorView} class provides a user interface for browsing and 
 * selecting available game levels.
 * <p>
 * It dynamically generates a grid of buttons based on the total level count defined 
 * in the {@link LevelLoader}. Upon selection, it handles the transition to the 
 * {@link GameView} with the appropriate level data.
 */
public class LevelSelectorView extends View {

    /** The current active instance of LevelSelectorView. */
    private static LevelSelectorView instance;

    /**
     * Constructs a new LevelSelectorView.
     * <p>
     * Initializes a {@link GridPane} that organizes level buttons into a 4-column layout.
     * Each button is configured with a click handler that triggers a FADE transition
     * to the chosen level.
     */
    public LevelSelectorView() {
        super();
        setInstance(this);

        Label title = new Label("Select a level");
        title.getStyleClass().add("text-title");

        GridPane levelSelectGrid = new GridPane();
        levelSelectGrid.getStyleClass().setAll("level-select-grid");

        // Iterate through total levels to build the selection grid
        for (int i = 0; i < LevelLoader.TOTAL_LEVELS; i++) {
            final int veryEffectivelyFinal = i + 1;
            Button levelSelectButton = new Button();
            levelSelectButton.setText(String.valueOf(veryEffectivelyFinal));
            levelSelectButton.setPrefSize(170, 170);
            levelSelectButton.setFocusTraversable(false);
            levelSelectButton.getStyleClass().setAll("level-select-button");
            
            // setOnAction is used over MouseClicked to ensure compatibility with 
            // different input methods (keyboard/controller/touch)
            levelSelectButton.setOnAction(actionEvent -> {
                AudioManager.playSoundEffect("button-click");
                try {
                    ViewManager.getInstance().switchView(
                            new GameView(LevelLoader.loadLevel(String.valueOf(veryEffectivelyFinal))),
                            TransitionType.FADE
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Arrange buttons in a grid with 4 columns
            levelSelectGrid.add(levelSelectButton, i % 4, i / 4);
        }

        VBox layout = new VBox(25);
        layout.getChildren().add(title);
        layout.getChildren().add(levelSelectGrid);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(50));

        root.getChildren().addAll(layout, new BackButton());
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     * Triggered when the view is displayed. Starts the menu background music.
     */
    @Override
    public void startup() {
        AudioManager.playMusic("music-menu");
    }

    /**
     * Cleans up resources when the view is destroyed or hidden.
     */
    @Override
    public void cleanup() {}

    /** * Returns the singleton instance of the LevelSelectorView.
     * @return the current {@code LevelSelectorView} instance.
     */
    public static LevelSelectorView getInstance() {
        return instance;
    }

    /** * Sets the singleton instance of the LevelSelectorView.
     * @param view the view instance to register.
     */
    public static void setInstance(LevelSelectorView view) {
        instance = view;
    }
}