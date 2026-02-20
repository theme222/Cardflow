package ui.mover;

import component.mover.Conveyor;
import component.mover.FlipFlop;
import component.mover.ParityFilter;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.GridPos;

public class ParityFilterRenderer extends Renderer<ParityFilter> {

    public static final ParityFilterRenderer INSTANCE =
            new ParityFilterRenderer();

    private static final double TILE_SIZE = 85;

    private ParityFilterRenderer() {}

    @Override
    protected double tileSize() {
        return TILE_SIZE;
    }

    public void render(ParityFilter parityFilter, Pane node, GridPos pos) {
        RenderState state = ParityFilterRenderResolver.resolve(parityFilter, pos, TILE_SIZE);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
