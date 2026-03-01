package ui.game;

import java.util.ArrayList;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import application.Game;
import application.view.GameView;
import component.GameTile;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;
import logic.GameLevel;
import registry.render.RenderLayer;
import ui.tooltip.Tooltip;
import ui.tooltip.TooltipLayer;
import util.Config;
import util.GridPos;

public class GameGrid extends GridPane {
    // gameGrid.getStyleClass().setAll("level-select-grid");
    public final GameTilePane[][] gameGridTilePanes;

    public void updateIfValid(GridPos pos) {
        if (pos.getY() < 0 || pos.getY() >= gameGridTilePanes.length)
            return;
        if (pos.getX() < 0 || pos.getX() >= gameGridTilePanes[0].length)
            return;

        gameGridTilePanes[pos.getY()][pos.getX()].updateUI();
    }

    public GameGrid(GameLevel level, RenderLayer layer, TooltipLayer tooltipLayer) {
        gameGridTilePanes = new GameTilePane[level.HEIGHT][level.WIDTH];
        this.setMouseTransparent(layer != RenderLayer.BASE);

        for (int i = 0; i < level.HEIGHT; i++) {
            for (int j = 0; j < level.WIDTH; j++) {

                GameTile tile = level.getTile(new GridPos(j, i));
                GameTilePane tilePane = new GameTilePane(tile, new GridPos(j, i), layer);
                gameGridTilePanes[i][j] = tilePane;

                this.add(tilePane, j, i);

                int row = i;
                int col = j;
                GridPos position =  new GridPos(col, row);

                if (layer == RenderLayer.BASE) {
                    tilePane.setMouseTransparent(false);
                    tilePane.setPickOnBounds(true);
                    tilePane.setOnMouseClicked(e -> {
                        Set<GridPos> dirty = Game.onTileClick(
                                level.getTile(position),
                                e.getButton(),
                                e.isShiftDown(),
                                e.isControlDown());
                        e.consume();
                        System.out.println("Dirty tiles: " + dirty);
                        dirty.forEach(GameView.getInstance()::updateTileAndAdjacent);
                    });

                    tooltipLayer.bind(tilePane, tile);
                }
            }

        }

        for (int c = 0; c < level.WIDTH; c++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(Config.TILE_SIZE);
            col.setPrefWidth(Config.TILE_SIZE);
            col.setMaxWidth(Config.TILE_SIZE);
            col.setHgrow(Priority.NEVER);
            this.getColumnConstraints().add(col);
        }

        for (int r = 0; r < level.HEIGHT; r++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(Config.TILE_SIZE);
            row.setPrefHeight(Config.TILE_SIZE);
            row.setMaxHeight(Config.TILE_SIZE);
            row.setVgrow(Priority.NEVER);
            this.getRowConstraints().add(row);
        }
    }
}
