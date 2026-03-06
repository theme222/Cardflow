package ui.modifier.pathway;

import component.modifier.changer.Arithmetic;
import component.modifier.pathway.Pathway;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.modifier.changer.ArithmeticRenderResolver;
import ui.modifier.changer.ArithmeticRenderer;
import ui.render.RenderState;
import ui.render.Renderer;
import util.Config;
import util.GridPos;

/**
 * Renderer for {@link Pathway} modifiers (entrances and exits).
 */
public class PathwayRenderer extends Renderer<Pathway> {


    /** Singleton instance of PathwayRenderer. */
    public static final PathwayRenderer INSTANCE =
            new PathwayRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private PathwayRenderer() {}

    /** 
     * Renders the pathway component.
     * 
     * @param pathway The pathway instance.
     * @param node The target Pane.
     * @param pos The grid position.
     * @param animated Whether the render is animated.
     */
    public void render(Pathway pathway, Pane node, GridPos pos, boolean animated) {
        RenderState state = PathwayRenderResolver.resolve(pathway, pos, Config.MODIFIER_ALPHA);
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
