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

/**
 * Renderer for {@link Combinator} modifiers.
 */
public class CombinatorRenderer extends Renderer<Combinator> {

    /** Singleton instance of CombinatorRenderer. */
    public static final CombinatorRenderer INSTANCE =
            new CombinatorRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private CombinatorRenderer() {}

    /** 
     * Renders the combinator onto the specified pane.
     * 
     * @param combinator The combinator to render.
     * @param node The target Pane.
     * @param pos The grid position.
     * @param animated Whether the render is animated.
     */
    public void render(Combinator combinator, Pane node, GridPos pos, boolean animated) {
        RenderState state = CombinatorRenderResolver.resolve(combinator, pos, Config.MODIFIER_ALPHA);
        draw(node, state);
    }

    /** 
     * Returns the modifier render layer.
     * 
     * @return {@link RenderLayer#MODIFIER}.
     */
    @Override
    public RenderLayer layer() {
        return RenderLayer.MODIFIER;
    }

}
