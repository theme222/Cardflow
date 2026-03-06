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

/**
 * Renderer for {@link Arithmetic} modifiers.
 * Draws the arithmetic symbol and overlays the numeric change value as text.
 */
public class ArithmeticRenderer extends Renderer<Arithmetic> {

    /** Singleton instance of ArithmeticRenderer. */
    public static final ArithmeticRenderer INSTANCE =
            new ArithmeticRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private ArithmeticRenderer() {}

    /** 
     * Renders the arithmetic modifier.
     * 
     * @param arithmetic The arithmetic instance.
     * @param node The target Pane.
     * @param pos The grid position.
     * @param animated Whether the render is animated.
     */
    public void render(Arithmetic arithmetic, Pane node, GridPos pos, boolean animated) {
        RenderState state = ArithmeticRenderResolver.resolve(arithmetic, pos, Config.MODIFIER_ALPHA);

        draw(node, state);
        textWithCanvas(node, arithmetic.getChange().toString(), state, (Canvas)node.getChildren().getFirst());
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
