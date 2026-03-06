package ui.modifier.changer;

import component.modifier.changer.Arithmetic;
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

public class ArithmeticRenderer extends Renderer<Arithmetic> {

    public static final ArithmeticRenderer INSTANCE =
            new ArithmeticRenderer();

    private ArithmeticRenderer() {}

    /** 
     * @param arithmetic
     * @param node
     * @param pos
     * @param animated
     */
    public void render(Arithmetic arithmetic, Pane node, GridPos pos, boolean animated) {
        RenderState state = ArithmeticRenderResolver.resolve(arithmetic, pos, Config.MODIFIER_ALPHA);

        draw(node, state);
        textWithCanvas(node, arithmetic.getChange().toString(), state, (Canvas)node.getChildren().getFirst());
    }

    /** 
     * @return RenderLayer
     */
    @Override
    public RenderLayer layer() {
        return RenderLayer.MODIFIER;
    }
}
