package ui.modifier.combinator;

import component.modifier.changer.Setter;
import component.modifier.changer.ValueSetter;
import component.modifier.combinator.Combinator;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.modifier.changer.SetterRenderResolver;
import ui.modifier.changer.SetterRenderer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

public class CombinatorRenderer extends Renderer<Combinator> {

    public static final CombinatorRenderer INSTANCE =
            new CombinatorRenderer();

    private CombinatorRenderer() {}

    /** 
     * @param combinator
     * @param node
     * @param pos
     * @param animated
     */
    public void render(Combinator combinator, Pane node, GridPos pos, boolean animated) {
        RenderState state = CombinatorRenderResolver.resolve(combinator, pos, Config.MODIFIER_ALPHA);
        draw(node, state);
    }

    /** 
     * @return RenderLayer
     */
    @Override
    public RenderLayer layer() {
        return RenderLayer.MODIFIER;
    }

}
