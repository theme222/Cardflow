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
import ui.overlay.SelectedTileOverlayRenderer;
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
        this.setMouseTransparent(layer != RenderLayer.MOUSE_EVENTS);
        this.setPickOnBounds(layer == RenderLayer.MOUSE_EVENTS);

        for (int i = 0; i < level.HEIGHT; i++) {
            for (int j = 0; j < level.WIDTH; j++) {

                GameTile tile = level.getTile(new GridPos(j, i));
                GameTilePane tilePane = new GameTilePane(tile, new GridPos(j, i), layer);
                gameGridTilePanes[i][j] = tilePane;

                this.add(tilePane, j, i);

                int row = i;
                int col = j;
                GridPos position = new GridPos(col, row);

                if (layer == RenderLayer.MOUSE_EVENTS) {
                    tooltipLayer.bind(tilePane, tile);
                }
            }

        }

        if (layer == RenderLayer.MOUSE_EVENTS) {
            this.setMouseTransparent(false);
            this.setPickOnBounds(true);

            this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                GridPos pos = getGridPosFromMouse(e);
                application.controller.PlacementController.INSTANCE.handleMousePressed(e, pos);
            });
            this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
                GridPos pos = getGridPosFromMouse(e);
                application.controller.PlacementController.INSTANCE.handleMouseDragged(e, pos);
            });
            this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                GridPos pos = getGridPosFromMouse(e);
                application.controller.PlacementController.INSTANCE.handleMouseReleased(e, pos);
            });
            this.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
                GridPos pos = getGridPosFromMouse(e);
                application.controller.PlacementController.INSTANCE.handleOnMouseMove(pos);
            });
            this.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                GridPos pos = getGridPosFromMouse(e);
                application.controller.PlacementController.INSTANCE.handleOnMouseExit(pos);
            });
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

    private GridPos getGridPosFromMouse(MouseEvent e) {

        double x = e.getX();
        double y = e.getY();

        int col = (int) (x / Config.TILE_SIZE);
        int row = (int) (y / Config.TILE_SIZE);

        if (row < 0 || row >= gameGridTilePanes.length)
            return null;
        if (col < 0 || col >= gameGridTilePanes[0].length)
            return null;

        return new GridPos(col, row);
    }
}
