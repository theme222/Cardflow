package application.scene;

import application.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import logic.GameLevel;
import util.LevelLoader;

import java.io.IOException;

public class LevelSelectorScene {
    public static Scene create() {

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
                    SceneManager.getInstance().showGame(LevelLoader.loadLevel(String.valueOf(veryEffectivelyFinal)));
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

        StackPane root = new StackPane();
        root.getChildren().add(layout);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(new Text("LevelSelectorScene")); // TODO: DEBUG

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case BACK_SPACE -> {
                    try {
                        GameLevel sandbox = LevelLoader.loadLevel("sandbox");
                        SceneManager.getInstance().showGame(sandbox); // or whatever your level swap method is
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return scene;
    }
}
