package ui.base;

import component.GameTile;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.card.CardRenderResolver;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

public class EmptyTileRenderer extends Renderer<GameTile> {
    public static final EmptyTileRenderer INSTANCE = new EmptyTileRenderer();

    public static final Image EMPTY_TILE_IMAGE = new Image(
        EmptyTileRenderer.class.getResourceAsStream("/asset/tiles/base/empty-tile.png"),
0, 0, true, false
    );

    public EmptyTileRenderer() {}

    /** 
     * @param tile
     * @param node
     * @param pos
     * @param animating
     */
    public void render(GameTile tile, Pane node, GridPos pos, boolean animating) {
        draw(node, new RenderState(EMPTY_TILE_IMAGE, Config.TILE_SIZE, Config.TILE_SIZE, 0, 0, 0, false, false, 1.0));
    }

    /** 
     * @return RenderLayer
     */
    @Override
    public RenderLayer layer() {
        return RenderLayer.BASE;
    }

}
