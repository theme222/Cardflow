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

public class PathwayRenderer extends Renderer<Pathway> {


    public static final PathwayRenderer INSTANCE =
            new PathwayRenderer();

    private PathwayRenderer() {}

    /** 
     * @param pathway
     * @param node
     * @param pos
     * @param animated
     */
    public void render(Pathway pathway, Pane node, GridPos pos, boolean animated) {
        RenderState state = PathwayRenderResolver.resolve(pathway, pos, Config.MODIFIER_ALPHA);
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
