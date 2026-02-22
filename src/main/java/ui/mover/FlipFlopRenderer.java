package ui.mover;

import component.mover.Conveyor;
import component.mover.FlipFlop;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

public class FlipFlopRenderer extends Renderer<FlipFlop> {

    public static final FlipFlopRenderer INSTANCE =
            new FlipFlopRenderer();

    private FlipFlopRenderer() {}

    @Override
    protected double tileSize() {
        return Config.TILE_SIZE;
    }

    public void render(FlipFlop flipFlop, Pane node, GridPos pos) {
        RenderState state = FlipFlopRenderResolver.resolve(flipFlop, pos, 1);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
