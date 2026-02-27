package application.view;

import application.TransitionType;
import application.ViewManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ui.button.BackButton;
import util.LevelLoader;

import java.io.IOException;

public class LevelSelectorView extends View {

    private static LevelSelectorView instance;

    public LevelSelectorView() {
        super();
        setInstance(this);

        Label title = new Label("Select a level");

        GridPane levelSelectGrid = new GridPane();
        levelSelectGrid.getStyleClass().setAll("level-select-grid");

        for (int i = 0; i < LevelLoader.TOTAL_LEVELS; i++) {
            final int veryEffectivelyFinal = i + 1;
            Button levelSelectButton = new Button();
            levelSelectButton.setText(String.valueOf(veryEffectivelyFinal));
            levelSelectButton.setPrefSize(170, 170);
            levelSelectButton.setFocusTraversable(false);
            levelSelectButton.getStyleClass().setAll("level-select-button");
            levelSelectButton.setOnAction(actionEvent -> {
                try {
                    ViewManager.getInstance().switchView(
                            new GameView(LevelLoader.loadLevel(String.valueOf(veryEffectivelyFinal))),
                            TransitionType.FADE
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }); // Always use setOnAction instead of setOnMouseClicked

            levelSelectGrid.add(levelSelectButton, i % 5, i / 5);
        }

        VBox layout = new VBox(10);
        layout.getChildren().add(title);
        layout.getChildren().add(levelSelectGrid);
        layout.setAlignment(Pos.TOP_CENTER);

        layout.setPadding(new Insets(20));

        root.getChildren().addAll(layout, new BackButton());
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @Override
    public void cleanup() {}

    public static LevelSelectorView getInstance() {
        return instance;
    }

    public static void setInstance(LevelSelectorView view) {
        instance = view;
    }
}
