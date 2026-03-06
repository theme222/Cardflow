package ui.mover;

import component.mover.ParityFilter;
import javafx.scene.image.Image;
import ui.render.RenderState;
import util.Direction;
import util.GridPos;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the visual state of a {@link ParityFilter} mover.
 */
public final class ParityFilterRenderResolver extends MoverRenderResolver {

    /**
     * Inner class for loading parity filter images.
     */
    private static class ParityFilterImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/parityfilter/";
        private static final String[] FILENAMES = {"-base", "-merge-a", "-merge-b", "-merge-c"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR + "parityfilter", FILENAMES, images, ".png");
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private ParityFilterRenderResolver() {}

    /** 
     * Resolves the render state for a parity filter.
     * 
     * @param parityFilter The filter instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the filter.
     */
    public static RenderState resolve(
            ParityFilter parityFilter,
            GridPos pos,
            double alpha
    ) {

        EnumSet<Direction> topology = MoverTopology.resolve(parityFilter, pos);
        SpriteData sprite = SpriteSelector.filter(topology, ParityFilterImage.images);

        double rotation = rotationFor(parityFilter) + sprite.rotationOffset();

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
