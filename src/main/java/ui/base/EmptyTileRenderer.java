package ui.base;

import component.GameTile;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.Renderer;
import util.GridPos;

public class EmptyTileRenderer extends Renderer<GameTile> {
    public static final EmptyTileRenderer INSTANCE = new EmptyTileRenderer();

    public EmptyTileRenderer() {}

    @Override
    public void render(GameTile tile, Pane node, GridPos pos, boolean animating) {
        //node.setText("norway" + tile.toString());
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.BASE;
    }

    @Override
    protected double tileSize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tileSize'");
    }
}
