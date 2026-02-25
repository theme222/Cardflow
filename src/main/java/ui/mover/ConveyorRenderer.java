package ui.mover;

import component.mover.Conveyor;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

public class ConveyorRenderer extends Renderer<Conveyor> {

    public static final ConveyorRenderer INSTANCE =
            new ConveyorRenderer();

    private ConveyorRenderer() {}


    public void render(Conveyor conveyor, Pane node, GridPos pos, boolean animating) {
        RenderState state = ConveyorRenderResolver.resolve(conveyor, pos, 1);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
