package application.view;

import application.ViewManager;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import logic.GameLevel;
import logic.PlayerInventory;
import ui.button.BackButton;
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

public class GameView extends View {

    private static GameView instance; // This value can be null and can be invalid (just because instance exists doesn't mean that it is the current view. You must check it on the viewManager.)
    private GameTilePane[][] gameGridTilePanes;
    private InventoryPane inventoryPane;

    public void updateTileAndAdjacent(GridPos pos) {
        updateIfValid(pos);

        updateIfValid(pos.addDirection(Direction.RIGHT)); // right
        updateIfValid(pos.addDirection(Direction.LEFT)); // left
        updateIfValid(pos.addDirection(Direction.DOWN)); // down
        updateIfValid(pos.addDirection(Direction.UP)); // up
    }

    private void updateIfValid(GridPos pos) {
        if (pos.getY() < 0 || pos.getY() >= gameGridTilePanes.length)
            return;
        if (pos.getX() < 0 || pos.getX() >= gameGridTilePanes[0].length)
            return;

        gameGridTilePanes[pos.getY()][pos.getX()].updateUI();
    }

    public GameView(GameLevel level) {
        super();
        setInstance(this);

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


        root.getChildren().addAll(mainLayout, new BackButton());
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public GameTilePane[][] getGameGridTilePanes() {
        return gameGridTilePanes;
    }

    public void setGameGridTilePanes(GameTilePane[][] gameGridTilePanes) {
        this.gameGridTilePanes = gameGridTilePanes;
    }

    public InventoryPane getInventoryPane() {
        return inventoryPane;
    }

    public void setInventoryPane(InventoryPane inventoryPane) {
        this.inventoryPane = inventoryPane;
    }

    public static GameView getInstance() {
        return instance;
    }

    public static void setInstance(GameView view) {
        instance = view;
    }
}
