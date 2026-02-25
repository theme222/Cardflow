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

public class RedBlackFilterRenderer extends Renderer<RedBlackFilter> {

    public static final RedBlackFilterRenderer INSTANCE =
            new RedBlackFilterRenderer();

    private RedBlackFilterRenderer() {}

    public void render(RedBlackFilter redBlackFilter, Pane node, GridPos pos) {
        RenderState state = RedBlackFilterRenderResolver.resolve(redBlackFilter, pos, 1);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
