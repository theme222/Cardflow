package ui.game;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import application.Game;
import application.view.GameView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import logic.GameLevel;
import registry.render.RenderLayer;
import util.Direction;
import util.GridPos;

public class GameRenderStack extends StackPane {

    private final EnumMap<RenderLayer, GameGrid> layers = new EnumMap<>(RenderLayer.class);

    public void updateIfValid(GridPos pos) {
        for (GameGrid grid : layers.values()) {
            grid.updateIfValid(pos);
        }
    }

    public GameRenderStack(GameLevel level) {
        super();
        initLayers(level);
    }

    private void initLayers(GameLevel level) {

        for (RenderLayer layer : RenderLayer.values()) {

            GameGrid pane;

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

            } else {

                pane = new GameGrid(level, layer, null);
                pane.setMouseTransparent(true);
            }

            layers.put(layer, pane);
            getChildren().add(pane);
        }
    }

}
