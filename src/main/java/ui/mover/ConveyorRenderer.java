package ui.mover;

import component.mover.Conveyor;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.GridPos;

public class ConveyorRenderer extends Renderer<Conveyor> {

    public static final ConveyorRenderer INSTANCE =
            new ConveyorRenderer();

    private static final double TILE_SIZE = 85;

    private ConveyorRenderer() {}

    @Override
    protected double tileSize() {
        return TILE_SIZE;
    }

    public void render(Conveyor conveyor, Pane node, GridPos pos, boolean animating) {
        RenderState state = ConveyorRenderResolver.resolve(conveyor, pos, TILE_SIZE);
        draw(node, state);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
