package ui.mover;

import component.mover.*;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

public class DelayRenderer extends Renderer<Delay> {

    public static final DelayRenderer INSTANCE =
            new DelayRenderer();

    private DelayRenderer() {}

    public void render(Delay delay, Pane node, GridPos pos) {
        RenderState state = DelayRenderResolver.resolve(delay, pos, 1);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
