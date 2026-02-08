package application.scene;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import logic.GameLevel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ui.GameTilePane;
import util.GridPos;

public class GameScene {

    static GameTilePane[][] gameGridTilePanes;

    public static Scene create(GameLevel level) {

        GameLevel.setInstance(level); // Most components will rely on this

        // TODO: MODIFY THIS TO BE A REGULAR PANE AND ADD A TILING MANAGER TO ALLOW GOOD LOOKING ANIMS AND STUFF :D
        GridPane gameGrid = new GridPane();
        gameGrid.getStyleClass().setAll("level-select-grid");

        gameGridTilePanes = new GameTilePane[level.HEIGHT][level.WIDTH];

        for (int i = 0; i < level.HEIGHT; i++) {
            for (int j = 0; j < level.WIDTH; j++) {
                GameTilePane tilePane = new GameTilePane(level.getTile(new GridPos(j, i)));
                gameGridTilePanes[i][j] = tilePane;

                gameGrid.add(tilePane, j, i);
            }
        }

        VBox infoPane = new VBox();
        infoPane.setPadding(new Insets(10));

        HBox mainLayout = new HBox();
        mainLayout.getChildren().addAll(gameGrid, infoPane);

        mainLayout.setPadding(new Insets(30));
        mainLayout.setSpacing(30);
        mainLayout.setAlignment(Pos.BASELINE_CENTER);

        StackPane root = new StackPane();
        root.getChildren().add(mainLayout);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(new Text("GameScene")); // TODO: DEBUG

        Scene scene = new Scene(root, 1920, 1080);

        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.SPACE),
                () -> {
                    level.doTick();
                    for (GridPos point : level.changedPoints) {
                        gameGridTilePanes[point.getY()][point.getX()].updateUI();
                    }
                }
        );

        return scene;
    }
}
