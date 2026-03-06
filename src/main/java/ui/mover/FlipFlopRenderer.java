package ui.mover;

import component.mover.Conveyor;
import component.mover.FlipFlop;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.card.CardRenderResolver;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

/**
 * Renderer for the {@link FlipFlop} mover.
 * Composites multiple layers (floor and overlay) to represent the flip-flop's state.
 */
public class FlipFlopRenderer extends Renderer<FlipFlop> {

    /** Singleton instance of FlipFlopRenderer. */
    public static final FlipFlopRenderer INSTANCE =
            new FlipFlopRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private FlipFlopRenderer() {}

    /** 
     * Renders the FlipFlop mover.
     * 
     * @param flipFlop The flip-flop instance.
     * @param node The target Pane.
     * @param pos The grid position.
     * @param animating Whether the rendering is part of an animation.
     */
    public void render(FlipFlop flipFlop, Pane node, GridPos pos, boolean animating) {
        RenderState floorState = FlipFlopRenderResolver.resolveFloor(flipFlop, flipFlop.getGridPos(), 1);
        RenderState overlayState = FlipFlopRenderResolver.resolveOverlay(flipFlop, flipFlop.getGridPos(), 1);

        Canvas canvas = new Canvas(floorState.width(), floorState.height());

        // draw the floor
        // Calling draw with canvas manually to allow multiple draws on top of each other
        drawWithCanvas(node, floorState, canvas, true);
        drawWithCanvas(node, overlayState, canvas, true);
        node.getChildren().setAll(canvas);
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
