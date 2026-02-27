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
import util.Config;
import util.Direction;
import util.GridPos;

public class GameRenderStack extends StackPane {

    private final EnumMap<RenderLayer, Pane> layers = new EnumMap<>(RenderLayer.class);

    public void updateIfValid(GridPos pos) {
        for (Pane pane : layers.values()) {
            if (pane instanceof GameGrid) {
                ((GameGrid) pane).updateIfValid(pos);
            }
        }
    }

    public GameRenderStack(GameLevel level) {
        super();
        initLayers(level);
    }

    private void initLayers(GameLevel level) {

        for (RenderLayer layer : RenderLayer.values()) {

            Pane pane;

            if (layer == RenderLayer.BASE) {

                pane = new GameGrid(level, layer,
                        (e, pos) -> {
                            System.out.println("2 Clicked tile at " + pos);
                            Set<GridPos> dirty = Game.onTileClick(
                                    level.getTile(pos),
                                    e.getButton(),
                                    e.isShiftDown(),
                                    e.isControlDown());
                            e.consume();
                            System.out.println("Dirty tiles: " + dirty);
                            dirty.forEach(GameView.getInstance()::updateTileAndAdjacent);
                        });

                pane.setMouseTransparent(false);

            } else if (FloatingLayerRegistry.INSTANCE.isFloating(layer)) {
                pane = new Pane();
                pane.setPrefSize(Config.TILE_SIZE * level.WIDTH, Config.TILE_SIZE * level.HEIGHT);
                pane.setMouseTransparent(true);
                FloatingLayerRegistry.INSTANCE.registerEntry(layer, pane);
            } else{
                pane = new GameGrid(level, layer, null);
                pane.setMouseTransparent(true);
            }

            layers.put(layer, pane);
            pane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            getChildren().add(pane);
        }
    }

}
