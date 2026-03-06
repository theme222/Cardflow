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
import ui.overlay.SelectedTileOverlayRenderer;
import ui.render.Renderer;
import util.GridPos;

/**
 * A specialized JavaFX Pane that represents a single tile on the game grid for a specific render layer.
 */
public class GameTilePane extends Pane {

    /** The logic tile associated with this pane. */
    private GameTile tile;
    /** The grid position of this tile. */
    private GridPos pos;
    /** The render layer this pane belongs to. */
    private RenderLayer layer;

    /**
     * Constructs a new GameTilePane.
     * 
     * @param tile The {@link GameTile} data.
     * @param pos The {@link GridPos} on the grid.
     * @param layer The {@link RenderLayer} this pane renders.
     */
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

    /**
     * Triggers a UI update for this tile and its adjacent neighbors.
     */
    public void updateUIAdjacent() {
        GameView.getInstance().updateTileAndAdjacent(tile.getGridPos());
    }

    /**
     * Updates the content of this pane based on the current state of the associated tile and the render layer.
     */
    public void updateUI() {
        clearLayers();

        GameTile tile = this.tile;
        switch (layer) {
            case RenderLayer.BASE:
                render(RenderLayer.BASE, tile);
                break;
            case RenderLayer.OVERLAY:
                break;
            case RenderLayer.CARD:
                if(tile.getCard() != null)
                    render(RenderLayer.CARD, tile.getCard());
                break;
            case RenderLayer.MOVER:
                if(tile.getMover() != null){
                    render(RenderLayer.MOVER, tile.getMover());
                }
                break;
            case RenderLayer.MODIFIER:
                if(tile.getModifier() != null)
                    render(RenderLayer.MODIFIER, tile.getModifier());
                break;

            case RenderLayer.MOUSE_OVERLAY:
                    // Custom Renderer
                    SelectedTileOverlayRenderer.INSTANCE.render(this,pos);
                break;
            
            default:
                break;
        }
    }

    /**
     * Clears all child nodes from this pane.
     */
    private void clearLayers() {
        getChildren().clear();
    }

    /** 
     * Renders a specific object using the appropriate renderer from the registry.
     * 
     * @param layer The layer to render.
     * @param obj The object to be rendered.
     */
    private void render(RenderLayer layer, Object obj) {
        Renderer<Object> renderer = RendererRegistry.INSTANCE.getRenderer(obj);

        if (renderer.layer() != layer)
            return;

        renderer.render(obj, this, pos);
    }

    /** 
     * Gets the associated game tile info.
     * 
     * @return The {@link GameTile}.
     */
    public GameTile getGameTileInfo() {
        return tile;
    }

    /** 
     * Sets the associated game tile info.
     * 
     * @param gameTileInfo The new {@link GameTile}.
     */
    public void setGameTileInfo(GameTile gameTileInfo) {
        this.tile = gameTileInfo;
    }

//    public String toString() {
//        return "GameTilePane " + layer;
//    }
}
