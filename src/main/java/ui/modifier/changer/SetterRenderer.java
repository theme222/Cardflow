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
import util.Helper;

public class SetterRenderer extends Renderer<Setter<?>> {

    public static final SetterRenderer INSTANCE =
            new SetterRenderer();

    private SetterRenderer() {}

    public void render(Setter<?> setter, Pane node, GridPos pos, boolean animated) {
        RenderState state = SetterRenderResolver.resolve(setter, pos, Config.MODIFIER_ALPHA);
        draw(node, state);
        if (setter instanceof ValueSetter valueSetter)
            textWithCanvas(node, Helper.getValueAsString(valueSetter.getChange()).toUpperCase(), state, (Canvas)node.getChildren().getFirst());

    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MODIFIER;
    }
}
