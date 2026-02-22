package ui.modifier.changer;

import component.modifier.changer.ValueSetter;
import component.modifier.changer.Setter;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

public class SetterRenderer extends Renderer<Setter<?>> {

    public static final SetterRenderer INSTANCE =
            new SetterRenderer();

    private SetterRenderer() {}

    @Override
    protected double tileSize() {
        return Config.TILE_SIZE;
    }

    public void render(Setter<?> setter, Pane node, GridPos pos) {
        RenderState state = SetterRenderResolver.resolve(setter, pos, Config.MODIFIER_ALPHA);
        draw(node, state);
        if (setter instanceof ValueSetter)
            textWithCanvas(node, setter.getChange().toString(), state, (Canvas)node.getChildren().getFirst());

    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MODIFIER;
    }
}
