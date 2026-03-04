package ui.overlay;

import java.util.function.BiFunction;

import application.view.GameView;
import component.GameTile;
import component.mover.Mover;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import logic.GameLevel;
import logic.event.card.TileSelectChangeEvent;
import registry.render.RendererRegistry;
import ui.render.Renderer;
import util.Direction;
import util.GridPos;

public class SelectedTileOverlayRenderer {

    public static final SelectedTileOverlayRenderer INSTANCE = 
        new SelectedTileOverlayRenderer();

    // Selected tile data
    private String tile;
    private BiFunction<String, Direction, Mover> moverFactory;
    private String moverName;
    private Direction rotation;

    private GridPos startPosition;
    private GridPos lastPosition;
    private GridPos endPosition;

    // Ghost node (reuse instead of recreating)

    private SelectedTileOverlayRenderer() {}

    /* ===============================
       Event Handlers
       =============================== */

    public void handleOnMouseEnter(GridPos pos) {
        setEndPosition(pos);
        if(!pos.equals(lastPosition)){
            GameView.getInstance().updateTileAndAdjacent(pos);
            if(lastPosition != null)
                GameView.getInstance().updateTileAndAdjacent(lastPosition);
            lastPosition = pos;
        }

        
    }

    public void handleOnMouseExit(GridPos pos) {
        if(endPosition.equals(pos)){
            setEndPosition(new GridPos(-1,-1)); // shove it into oob :)
            GameView.getInstance().updateTileAndAdjacent(pos);
        }
    }

    public void handleOnCardChange(TileSelectChangeEvent e) {
        this.moverFactory = e.getFactory();
        this.moverName = e.getMovements();
        this.rotation = e.getRotation();
        GameView.getInstance().updateTileAndAdjacent(endPosition);
    }

    /* ===============================
       Rendering
       =============================== */

    public void render(Pane overlayPane,GridPos pos) {

        if (moverFactory == null || moverName == null) {
            return; // nothing selected
        }

        // Lazy create ghost tile
        if (endPosition != null && endPosition.equals(pos)) {
            Mover mover = moverFactory.apply(moverName, rotation);
            Renderer<Mover> renderer = RendererRegistry.INSTANCE.getRenderer(mover);
            if(GameLevel.getInstance().getTile(pos).getMover() != null){
                DeleteOverlay.INSTANCE.render(overlayPane);
            }else{
                renderer.render(mover, overlayPane, pos);
                overlayPane.setOpacity(0.5); // 50% transparent
            }
        }
    }

    /* ===============================
       Position Helpers
       =============================== */

    public void setStartPosition(GridPos pos) {
        startPosition = pos;
    }

    public void setEndPosition(GridPos pos) {
        endPosition = pos;
    }
}