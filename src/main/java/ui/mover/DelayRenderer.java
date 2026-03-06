package ui.mover;

import component.mover.*;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

/**
 * Renderer for the {@link Delay} mover.
 */
public class DelayRenderer extends Renderer<Delay> {

    /** Singleton instance of DelayRenderer. */
    public static final DelayRenderer INSTANCE =
            new DelayRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private DelayRenderer() {}

    /** 
     * Renders the delay mover.
     * 
     * @param delay The delay instance.
     * @param node The target Pane.
     * @param pos The grid position.
     * @param animated Whether the render is animated.
     */
    public void render(Delay delay, Pane node, GridPos pos, boolean animated) {
        RenderState state = DelayRenderResolver.resolve(delay, pos, 1);
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
