package ui.mover;

import component.mover.Conveyor;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

/**
 * Renderer for the {@link Conveyor} mover.
 */
public class ConveyorRenderer extends Renderer<Conveyor> {

    /** Singleton instance of ConveyorRenderer. */
    public static final ConveyorRenderer INSTANCE =
            new ConveyorRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private ConveyorRenderer() {}


    /** 
     * Renders the conveyor mover.
     * 
     * @param conveyor The conveyor instance.
     * @param node The target Pane.
     * @param pos The grid position.
     * @param animating Whether the rendering is part of an animation.
     */
    public void render(Conveyor conveyor, Pane node, GridPos pos, boolean animating) {
        RenderState state = ConveyorRenderResolver.resolve(conveyor, pos, 1);
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
