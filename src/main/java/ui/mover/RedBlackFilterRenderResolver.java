package ui.mover;

import component.mover.RedBlackFilter;
import javafx.scene.image.Image;
import ui.render.RenderState;
import util.Direction;
import util.GridPos;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the visual state of a {@link RedBlackFilter} mover.
 */
public final class RedBlackFilterRenderResolver extends MoverRenderResolver {

    /**
     * Inner class for loading red-black filter images.
     */
    private static class RedBlackFilterImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/redblackfilter/";
        private static final String[] FILENAMES = {"-base", "-merge-a", "-merge-b", "-merge-c"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR + "redblackfilter", FILENAMES, images, ".png");
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private RedBlackFilterRenderResolver() {}

    /** 
     * Resolves the render state for a red-black filter.
     * 
     * @param redBlackFilter The filter instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the filter.
     */
    public static RenderState resolve(
            RedBlackFilter redBlackFilter,
            GridPos pos,
            double alpha
    ) {
        EnumSet<Direction> topology = MoverTopology.resolve(redBlackFilter, pos);
        SpriteData sprite = SpriteSelector.filter(topology, RedBlackFilterImage.images);

        double rotation = rotationFor(redBlackFilter) + sprite.rotationOffset();

        return new RenderState(
                sprite.image(),
                85,
                85,
                0,
                0,
                rotation,
                sprite.mirrorX(),
                false,
                alpha
        );
    }
}
