package ui.modifier.changer;

import component.modifier.changer.*;
import component.mover.Conveyor;
import javafx.scene.image.Image;
import ui.mover.MoverRenderResolver;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.Config;
import util.GridPos;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the visual state of an {@link Arithmetic} modifier.
 * Handles the selection of symbols for addition, subtraction, multiplication, and division.
 */
public final class ArithmeticRenderResolver extends RenderResolver {

    /**
     * Inner class for loading arithmetic modifier images.
     */
    private static class ArithmeticImage {
        private static final String RESOURCE_DIR = "/asset/tiles/modifier/changer/arithmetic/";
        private static final String[] FILENAMES = {"modify-adder", "modify-subtractor", "modify-multiplier", "modify-divider"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }


    /**
     * Private constructor to prevent instantiation.
     */
    private ArithmeticRenderResolver() {}

    /** 
     * Resolves the render state for an arithmetic modifier.
     * 
     * @param arithmetic The arithmetic modifier instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the modifier.
     */
    public static RenderState resolve(
            Arithmetic arithmetic,
            GridPos pos,
            double alpha
    ) {

        Image symbol = ArithmeticImage.images.get("modify-" + arithmetic.getClass().getSimpleName().toLowerCase());

        return new RenderState(
                symbol,
                Config.TILE_SIZE,
                Config.TILE_SIZE,
                0,
                0,
                0,
                false,
                arithmetic.isDisabled(),
                alpha
        );
    }


}
