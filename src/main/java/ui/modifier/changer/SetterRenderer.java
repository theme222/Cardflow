package ui.modifier.changer;

import component.modifier.changer.Arithmetic;
import component.modifier.changer.Setter;
import component.modifier.changer.SuitSetter;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
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
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MODIFIER;
    }
}
