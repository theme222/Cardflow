package ui.mover;

import component.mover.Conveyor;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

public class ConveyorRenderer extends Renderer<Conveyor> {
    // I'm starting to realize after I've already copy and pasted all these files that I could have done it in one.

    public static final ConveyorRenderer INSTANCE =
            new ConveyorRenderer();

    private ConveyorRenderer() {}

    @Override
    protected double tileSize() {
        return Config.TILE_SIZE;
    }

    public void render(Conveyor conveyor, Pane node, GridPos pos) {
        RenderState state = ConveyorRenderResolver.resolve(conveyor, pos, 1);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
