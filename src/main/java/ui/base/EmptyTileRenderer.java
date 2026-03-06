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

/**
 * A renderer for empty game tiles.
 * Displays the default tile image when no other components are present on a tile.
 */
public class EmptyTileRenderer extends Renderer<GameTile> {
    /**
     * The singleton instance of EmptyTileRenderer.
     */
    public static final EmptyTileRenderer INSTANCE = new EmptyTileRenderer();

    /**
     * The image used for rendering an empty tile.
     */
    public static final Image EMPTY_TILE_IMAGE = new Image(
        EmptyTileRenderer.class.getResourceAsStream("/asset/tiles/base/empty-tile.png"),
0, 0, true, false
    );

    /**
     * Constructs a new EmptyTileRenderer.
     */
    public EmptyTileRenderer() {}

    /** 
     * Renders the empty tile onto the specified pane.
     * 
     * @param tile The tile to render.
     * @param node The JavaFX Pane to render into.
     * @param pos The grid position of the tile.
     * @param animating Whether the rendering is part of an animation.
     */
    public void render(GameTile tile, Pane node, GridPos pos, boolean animating) {
        draw(node, new RenderState(EMPTY_TILE_IMAGE, Config.TILE_SIZE, Config.TILE_SIZE, 0, 0, 0, false, false, 1.0));
    }

    /** 
     * Gets the render layer for this renderer.
     * 
     * @return {@link RenderLayer#BASE}.
     */
    @Override
    public RenderLayer layer() {
        return RenderLayer.BASE;
    }

}
