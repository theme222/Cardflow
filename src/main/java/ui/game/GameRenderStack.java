package ui.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import application.Game;
import application.view.GameView;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import logic.GameLevel;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import ui.tooltip.TooltipLayer;
import util.Config;
import util.Direction;
import util.GridPos;

/**
 * A StackPane that manages all rendering layers of the game grid.
 * It coordinates updates across different layers like background, movers, cards, and overlays.
 */
public class GameRenderStack extends StackPane {

    /** Maps each {@link RenderLayer} to its corresponding Pane. */
    private final EnumMap<RenderLayer, Pane> layers = new EnumMap<>(RenderLayer.class);

    /** 
     * Triggers an update for a specific grid position across all layers.
     * 
     * @param pos The {@link GridPos} to update.
     */
    public void updateIfValid(GridPos pos) {
        for (Pane pane : layers.values()) {
            if (pane instanceof GameGrid) {
                ((GameGrid) pane).updateIfValid(pos);
            }
        }
    }

    /**
     * Constructs a new GameRenderStack.
     * 
     * @param level The current {@link GameLevel}.
     * @param tooltipLayer The {@link TooltipLayer} for binding tooltips to grid elements.
     */
    public GameRenderStack(GameLevel level, TooltipLayer tooltipLayer) {
        super();

        for (RenderLayer layer : RenderLayer.values()) {
            Pane pane;

            if (FloatingLayerRegistry.INSTANCE.isFloating(layer)) {
                pane = new Pane();
                pane.setPrefSize(Config.TILE_SIZE * level.WIDTH, Config.TILE_SIZE * level.HEIGHT);
                pane.setMouseTransparent(true);
                pane.setPickOnBounds(false);
                FloatingLayerRegistry.INSTANCE.registerEntry(layer, pane);
            }
            else {
                pane = new GameGrid(level, layer, tooltipLayer);
            }

            layers.put(layer, pane);
            pane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            getChildren().add(pane);
        }
    }

}
