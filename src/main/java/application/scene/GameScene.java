package application.scene;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import logic.GameLevel;
import logic.PlayerInventory;
import ui.inventory.InventoryPane;
import ui.render.GameTilePane;
import application.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import util.Direction;
import util.GridPos;

public class GameScene {

    public static GameTilePane[][] gameGridTilePanes;
    public static InventoryPane inventoryPane;

    public static void updateTileAndAdjacent(GridPos pos) {
        updateIfValid(pos);

        updateIfValid(pos.addDirection(Direction.RIGHT)); // right
        updateIfValid(pos.addDirection(Direction.LEFT)); // left
        updateIfValid(pos.addDirection(Direction.DOWN)); // down
        updateIfValid(pos.addDirection(Direction.UP)); // up
    }

    private static void updateIfValid(GridPos pos) {
        if (pos.getY() < 0 || pos.getY() >= gameGridTilePanes.length)
            return;
        if (pos.getX() < 0 || pos.getX() >= gameGridTilePanes[0].length)
            return;

        gameGridTilePanes[pos.getY()][pos.getX()].updateUI();
    }

    public static Scene create(GameLevel level) {

        GameLevel.setInstance(level); // Most components will rely on this
        PlayerInventory.setInstance(new PlayerInventory(level));

        // TODO: MODIFY THIS TO BE A REGULAR PANE AND ADD A TILING MANAGER TO ALLOW GOOD
        // LOOKING ANIMS AND STUFF :D
        GridPane gameGrid = new GridPane();
        // gameGrid.getStyleClass().setAll("level-select-grid");

        gameGridTilePanes = new GameTilePane[level.HEIGHT][level.WIDTH];

        for (int i = 0; i < level.HEIGHT; i++) {
            for (int j = 0; j < level.WIDTH; j++) {
                GameTilePane tilePane = new GameTilePane(level.getTile(new GridPos(j, i)));
                gameGridTilePanes[i][j] = tilePane;

                gameGrid.add(tilePane, j, i);
            }
        }

        for (int c = 0; c < level.WIDTH; c++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(85);
            col.setPrefWidth(85);
            col.setMaxWidth(85);
            col.setHgrow(Priority.NEVER);
            gameGrid.getColumnConstraints().add(col);
        }

        for (int r = 0; r < level.HEIGHT; r++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(85);
            row.setPrefHeight(85);
            row.setMaxHeight(85);
            row.setVgrow(Priority.NEVER);
            gameGrid.getRowConstraints().add(row);
        }

        VBox infoPane = new VBox();
        infoPane.setPadding(new Insets(10));

        inventoryPane = new InventoryPane(PlayerInventory.getInstance());

        HBox mainLayout = new HBox();
        mainLayout.getChildren().addAll(gameGrid, inventoryPane);

        mainLayout.setAlignment(Pos.BASELINE_CENTER);

        StackPane root = new StackPane();
        root.getChildren().add(mainLayout);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(new Text("GameScene")); // TODO: DEBUG

        Scene scene = new Scene(root);

        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.SPACE),
                () -> {
                    level.doTick();
                    for (GridPos point : level.changedPoints) { // TODO: THIS SHOULD BE MOVED TO GAMESTATE
                        gameGridTilePanes[point.getY()][point.getX()].updateUI();
                    }
                });
        
        scene.onMouseClickedProperty().set(event -> {
            Game.onSceneClick(
                    event.getButton(),
                    event.isShiftDown(),
                    event.isControlDown()
            );
        });

        

        return scene;
    }
}
