package ui.game;

import java.util.EnumMap;
import java.util.Set;

import application.Game;
import application.view.GameView;
import component.GameTile;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import registry.render.RenderLayer;
import registry.render.RendererRegistry;
import ui.render.Renderer;
import util.GridPos;

public class GameTilePane extends Pane {

    private GameTile tile;
    private GridPos pos;
    private RenderLayer layer;

    public GameTilePane(GameTile tile, GridPos pos, RenderLayer layer) {
        this.tile = tile;
        this.pos = pos;
        this.layer = layer;

        //getStyleClass().add("game-tile");
        setPrefSize(85, 85);
        setMaxSize(85, 85);
        setFocusTraversable(false);

        if(layer == RenderLayer.BASE) {
            getStyleClass().add("game-tile");
        }

        //Text debugText = new Text(10, 20, "Layer: " + layer + "\nPos: " + pos);
        //debugText.setStyle("-fx-fill: red; -fx-font-size: 14;");

        

        updateUI();

        //this.getChildren().add(debugText);
    }

    public void updateUIAdjacent() {
        GameView.getInstance().updateTileAndAdjacent(tile.getGridPos());
    }

    public void updateUI() {
        clearLayers();

        GameTile tile = this.tile;
        //System.out.println("Updating tile at " + pos + ": " + tile + " with card " + tile.getCard() + " and mover " + tile.getMover() + " on layer " + layer);
        switch (layer) {
            case RenderLayer.BASE:
                render(RenderLayer.BASE, tile);
                break;
            case RenderLayer.OVERLAY:
                //render(RenderLayer.OVERLAY, tile);
                break;
            case RenderLayer.CARD:
                if(tile.getCard() != null)
                    render(RenderLayer.CARD, tile.getCard());
                break;
            case RenderLayer.MOVER:
                if(tile.getMover() != null){
                    System.out.println("Rendering mover at " + pos + tile.getMover());
                    render(RenderLayer.MOVER, tile.getMover());
                }
                break;
            
            default:
                break;
        }
    }

    private void clearLayers() {
        getChildren().clear();
    }

    private void render(RenderLayer layer, Object obj) {
        Renderer<Object> renderer = RendererRegistry.INSTANCE.getRenderer(obj);

        if (renderer.layer() != layer)
            return;

        renderer.render(obj, this, pos);
    }

    public GameTile getGameTileInfo() {
        return tile;
    }

    public void setGameTileInfo(GameTile gameTileInfo) {
        this.tile = gameTileInfo;
    }
}
