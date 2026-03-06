package ui.modifier.pathway;

import component.modifier.pathway.Pathway;
import component.mover.Delay;
import javafx.scene.image.Image;
import ui.mover.DelayRenderResolver;
import ui.mover.MoverRenderResolver;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.Direction;
import util.GridPos;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the visual state of a {@link Pathway} modifier.
 */
public class PathwayRenderResolver extends RenderResolver {

    /**
     * Inner class for loading pathway images.
     */
    private static class PathwayImage {
        private static final String RESOURCE_DIR = "/asset/tiles/modifier/pathway/";
        private static final String[] FILENAMES = {"pathway-entrance", "pathway-exit"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private PathwayRenderResolver() {}

    /** 
     * Resolves the render state for a pathway.
     * 
     * @param pathway The pathway instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the pathway.
     */
    public static RenderState resolve(
            Pathway pathway,
            GridPos pos,
            double alpha
    ) {

        Image img = PathwayImage.images.get("pathway-" + pathway.getClass().getSimpleName().toLowerCase());

        return new RenderState(
                img,
                85,
                85,
                0,
                0,
                0,
                false,
                false,
                alpha
        );
    }
}
