package ui.mover;

import component.mover.Conveyor;
import component.mover.FlipFlop;
import component.mover.ParityFilter;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

/**
 * Renderer for the {@link ParityFilter} mover.
 */
public class ParityFilterRenderer extends Renderer<ParityFilter> {

    /** Singleton instance of ParityFilterRenderer. */
    public static final ParityFilterRenderer INSTANCE =
            new ParityFilterRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private ParityFilterRenderer() {}

    /** 
     * Renders the ParityFilter onto the specified pane.
     * 
     * @param parityFilter The filter to render.
     * @param node The Pane to render into.
     * @param pos The grid position.
     * @param animated Whether the render is animated.
     */
    public void render(ParityFilter parityFilter, Pane node, GridPos pos, boolean animated) {
        RenderState state = ParityFilterRenderResolver.resolve(parityFilter, pos, 1);
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
