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
import util.Util;

/**
 * Renderer for {@link Setter} modifiers.
 * Draws the base setter symbol and overlays the new value (for value setters) as text.
 */
public class SetterRenderer extends Renderer<Setter<?>> {

    /** Singleton instance of SetterRenderer. */
    public static final SetterRenderer INSTANCE =
            new SetterRenderer();

    /**
     * Private constructor to prevent instantiation.
     */
    private SetterRenderer() {}

    /** 
     * Renders the setter.
     * 
     * @param setter The setter instance.
     * @param node The target Pane.
     * @param pos The grid position.
     * @param animated Whether the render is animated.
     */
    public void render(Setter<?> setter, Pane node, GridPos pos, boolean animated) {
        RenderState state = SetterRenderResolver.resolve(setter, pos, Config.MODIFIER_ALPHA);
        draw(node, state);
        if (setter instanceof ValueSetter valueSetter)
            textWithCanvas(node, Util.getValueAsString(valueSetter.getChange()).toUpperCase(), state, (Canvas)node.getChildren().getFirst());

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
