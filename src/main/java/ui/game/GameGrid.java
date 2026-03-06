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

/**
 * A GridPane that represents a single layer of the game board.
 * Manages the layout of {@link GameTilePane}s and handles mouse events for the top-most layer.
 */
public class GameGrid extends GridPane {
    /** The 2D array of tile panes representing this grid layer. */
    public final GameTilePane[][] gameGridTilePanes;

    /** 
     * Updates the UI for the tile at the specified position if it is within bounds.
     * 
     * @param pos The {@link GridPos} to update.
     */
    public void updateIfValid(GridPos pos) {
        if (pos.getY() < 0 || pos.getY() >= gameGridTilePanes.length)
            return;
        if (pos.getX() < 0 || pos.getX() >= gameGridTilePanes[0].length)
            return;

        gameGridTilePanes[pos.getY()][pos.getX()].updateUI();
    }

    /**
     * Constructs a new GameGrid for a specific level and layer.
     * 
     * @param level The {@link GameLevel} to display.
     * @param layer The {@link RenderLayer} this grid represents.
     * @param tooltipLayer The {@link TooltipLayer} for tile tooltips.
     */
    public GameGrid(GameLevel level, RenderLayer layer, TooltipLayer tooltipLayer) {

        gameGridTilePanes = new GameTilePane[level.HEIGHT][level.WIDTH];

        configureLayer(layer);
        buildGrid(level, layer, tooltipLayer);
        registerMouseHandlers(layer);
        buildConstraints(level);
    }

    /** 
     * Configures mouse transparency and interaction based on the layer type.
     * 
     * @param layer The {@link RenderLayer}.
     */
    private void configureLayer(RenderLayer layer) {
        setMouseTransparent(layer != RenderLayer.MOUSE_EVENTS);
        setPickOnBounds(layer == RenderLayer.MOUSE_EVENTS);
    }

    /** 
     * Builds the grid of {@link GameTilePane}s.
     * 
     * @param level The level data.
     * @param layer The layer being built.
     * @param tooltipLayer The tooltip manager.
     */
    private void buildGrid(GameLevel level, RenderLayer layer, TooltipLayer tooltipLayer) {

        for (int i = 0; i < level.HEIGHT; i++) {
            for (int j = 0; j < level.WIDTH; j++) {

                GridPos pos = new GridPos(j, i);
                GameTile tile = level.getTile(pos);

                GameTilePane tilePane = new GameTilePane(tile, pos, layer);
                gameGridTilePanes[i][j] = tilePane;

                add(tilePane, j, i);

                if (layer == RenderLayer.MOUSE_EVENTS) {
                    tooltipLayer.bind(tilePane, tile);
                }
            }
        }
    } 

    /** 
     * Registers mouse event handlers if this is the interaction layer.
     * 
     * @param layer The layer to register handlers for.
     */
    private void registerMouseHandlers(RenderLayer layer) {

        if (layer != RenderLayer.MOUSE_EVENTS)
            return;

        setMouseTransparent(false);
        setPickOnBounds(true); 

        addEventHandler(MouseEvent.MOUSE_PRESSED, e -> application.controller.PlacementController.INSTANCE
                .handleMousePressed(e, getGridPosFromMouse(e)));

        addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> application.controller.PlacementController.INSTANCE
                .handleMouseDragged(e, getGridPosFromMouse(e)));

        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> application.controller.PlacementController.INSTANCE
                .handleMouseReleased(e, getGridPosFromMouse(e)));

        addEventHandler(MouseEvent.MOUSE_MOVED, e -> application.controller.PlacementController.INSTANCE
                .handleOnMouseMove(getGridPosFromMouse(e)));

        addEventHandler(MouseEvent.MOUSE_EXITED, e -> application.controller.PlacementController.INSTANCE
                .handleOnMouseExit(getGridPosFromMouse(e)));
    }

    /** 
     * Builds grid constraints based on level dimensions and tile size.
     * 
     * @param level The level data.
     */
    private void buildConstraints(GameLevel level) {

        for (int c = 0; c < level.WIDTH; c++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setMinWidth(Config.TILE_SIZE);
            col.setPrefWidth(Config.TILE_SIZE);
            col.setMaxWidth(Config.TILE_SIZE);
            col.setHgrow(Priority.NEVER);
            getColumnConstraints().add(col);
        }

        for (int r = 0; r < level.HEIGHT; r++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(Config.TILE_SIZE);
            row.setPrefHeight(Config.TILE_SIZE);
            row.setMaxHeight(Config.TILE_SIZE);
            row.setVgrow(Priority.NEVER);
            getRowConstraints().add(row);
        }
    }

    /** 
     * Translates a MouseEvent's coordinates into a grid position.
     * 
     * @param e The {@link MouseEvent}.
     * @return The corresponding {@link GridPos}, or {@code null} if out of bounds.
     */
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
