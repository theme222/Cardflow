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

public class GameRenderStack extends StackPane {

    private final EnumMap<RenderLayer, Pane> layers = new EnumMap<>(RenderLayer.class);

    /** 
     * @param pos
     */
    public void updateIfValid(GridPos pos) {
        for (Pane pane : layers.values()) {
            if (pane instanceof GameGrid) {
                ((GameGrid) pane).updateIfValid(pos);
            }
        }
    }

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
