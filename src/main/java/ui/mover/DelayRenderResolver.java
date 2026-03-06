package ui.mover;

import component.mover.Delay;
import component.mover.RedBlackFilter;
import javafx.scene.image.Image;
import ui.render.RenderState;
import util.Direction;
import util.GridPos;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the visual state of a {@link Delay} mover.
 */
public final class DelayRenderResolver extends MoverRenderResolver {

    /**
     * Inner class for loading delay mover images.
     */
    private static class DelayImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/delay/";
        private static final String[] FILENAMES = {"-base", "-turn", "-merge-a", "-merge-b", "-merge-c"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR + "delay", FILENAMES, images, ".png");
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private DelayRenderResolver() {}

    /** 
     * Resolves the render state for a delay mover.
     * 
     * @param delay The delay instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the mover.
     */
    public static RenderState resolve(
            Delay delay,
            GridPos pos,
            double alpha
    ) {

        EnumSet<Direction> topology = MoverTopology.resolve(delay, pos);
        SpriteData sprite = SpriteSelector.regular(topology, DelayImage.images);
        double rotation = rotationFor(delay) + sprite.rotationOffset();

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
