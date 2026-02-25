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

public class FlipFlopRenderer extends Renderer<FlipFlop> {

    public static final FlipFlopRenderer INSTANCE =
            new FlipFlopRenderer();

    private FlipFlopRenderer() {}

    public void render(FlipFlop flipFlop, Pane node, GridPos pos, boolean animating) {
        RenderState floorState = FlipFlopRenderResolver.resolveFloor(flipFlop, flipFlop.getGridPos(), 1);
        RenderState overlayState = FlipFlopRenderResolver.resolveOverlay(flipFlop, flipFlop.getGridPos(), 1);

        Canvas canvas = new Canvas(floorState.width(), floorState.height());

        // draw the floor
        // Calling draw with canvas manually to allow multiple draws on top of each other
        drawWithCanvas(node, floorState, canvas);
        drawWithCanvas(node, overlayState, canvas);
        node.getChildren().setAll(canvas);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
