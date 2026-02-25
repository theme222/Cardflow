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

public class ParityFilterRenderer extends Renderer<ParityFilter> {

    public static final ParityFilterRenderer INSTANCE =
            new ParityFilterRenderer();

    private ParityFilterRenderer() {}

    public void render(ParityFilter parityFilter, Pane node, GridPos pos, boolean animated) {
        RenderState state = ParityFilterRenderResolver.resolve(parityFilter, pos, 1);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
