package ui.render;

import java.util.EnumMap;
import java.util.Set;

import application.Game;
import application.scene.GameScene;
import component.GameTile;
import component.mover.Conveyor;
import component.mover.Mover;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import registry.render.RenderLayer;
import registry.render.RendererRegistry;
import util.GridPos;

public class GameTilePane extends Button {

    private final EnumMap<RenderLayer, Pane> layers = new EnumMap<>(RenderLayer.class);

    private final StackPane content = new StackPane();
    private GameTile gameTileInfo;

    public GameTilePane(GameTile gameTileInfo) {
        this.gameTileInfo = gameTileInfo;

        getStyleClass().add("game-tile");
        setPrefSize(85, 85);
        setMaxSize(85, 85);
        setFocusTraversable(false);

        initLayers();
        setGraphic(content);

        setOnMousePressed(e -> {
            Set<GridPos> dirty = Game.onTileClick(
                    gameTileInfo,
                    e.getButton(),
                    e.isShiftDown(),
                    e.isControlDown());
            e.consume();
            dirty.forEach(GameScene::updateTileAndAdjacent);
        });

        updateUI();
    }

    private void initLayers() {
        for (RenderLayer layer : RenderLayer.values()) {
            Pane pane = new Pane();
            pane.setPickOnBounds(false);
            pane.setMouseTransparent(true);
            pane.setManaged(false);
            pane.setPrefSize(85, 85);

            layers.put(layer, pane);
            content.getChildren().add(pane);
        }
    }

    public void updateUIAdjacent() {
        GameScene.updateTileAndAdjacent(gameTileInfo.getGridPos());
    }

    public void updateUI() {
        clearLayers();

        GameTile tile = gameTileInfo;

        render(RenderLayer.BASE, tile);

        if (tile.getMover() != null) {
            render(RenderLayer.MOVER, tile.getMover());
        }

        if (tile.getCard() != null) {
            System.out.println("Rendering card on tile " + tile.getGridPos());
            render(RenderLayer.CARD, tile.getCard());
        }
    }

    private void clearLayers() {
        layers.values().forEach(p -> p.getChildren().clear());
    }

    private void render(RenderLayer layer, Object obj) {
        Renderer<Object> renderer = RendererRegistry.INSTANCE.getRenderer(obj);

        if (renderer.layer() != layer)
            return;

        renderer.render(obj, layers.get(layer), gameTileInfo.getGridPos());
    }

    public GameTile getGameTileInfo() {
        return gameTileInfo;
    }

    public void setGameTileInfo(GameTile gameTileInfo) {
        this.gameTileInfo = gameTileInfo;
    }
}
