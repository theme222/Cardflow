package ui.mover;

import component.mover.Conveyor;
import component.mover.FlipFlop;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.GridPos;

public class FlipFlopRenderer extends Renderer<FlipFlop> {

    public static final FlipFlopRenderer INSTANCE =
            new FlipFlopRenderer();

    private static final double TILE_SIZE = 85;

    private FlipFlopRenderer() {}

    @Override
    protected double tileSize() {
        return TILE_SIZE;
    }

    public void render(FlipFlop flipFlop, Pane node, GridPos pos, boolean animating) {
        RenderState state = FlipFlopRenderResolver.resolve(flipFlop, pos, TILE_SIZE);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
