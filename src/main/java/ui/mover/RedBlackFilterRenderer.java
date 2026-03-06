package ui.mover;

import component.mover.Conveyor;
import component.mover.FlipFlop;
import component.mover.ParityFilter;
import component.mover.RedBlackFilter;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

/**
 * Renderer for the {@link RedBlackFilter} mover.
 */
public class RedBlackFilterRenderer extends Renderer<RedBlackFilter> {

    /** Singleton instance of RedBlackFilterRenderer. */
    public static final RedBlackFilterRenderer INSTANCE =
            new RedBlackFilterRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private RedBlackFilterRenderer() {}

    /** 
     * Renders the RedBlackFilter onto the specified pane.
     * 
     * @param redBlackFilter The filter to render.
     * @param node The Pane to render into.
     * @param pos The grid position.
     * @param animated Whether the render is animated.
     */
    public void render(RedBlackFilter redBlackFilter, Pane node, GridPos pos, boolean animated) {
        RenderState state = RedBlackFilterRenderResolver.resolve(redBlackFilter, pos, 1);
        draw(node, state);
    }

    /** 
     * Returns the mover render layer.
     * 
     * @return {@link RenderLayer#MOVER}.
     */
    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
