package ui.mover;

import component.mover.FlipFlop;
import javafx.scene.image.Image;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.Direction;
import util.GridPos;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Resolves the visual state of a {@link FlipFlop} mover.
 * Handles both the base floor image and the state-dependent overlays (red/blue).
 */
public final class FlipFlopRenderResolver extends MoverRenderResolver {

    /**
     * Inner class for loading flip-flop specific images.
     */
    private static class FlipFlopImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/flipflop/";
        private static final String[] FILENAMES = {"-base", "-merge-a", "-merge-c"};
        public static final Map<String, Image> floorImages = new HashMap<>();
        public static final Map<String, Image> overlayBlueImages = new HashMap<>();
        public static final Map<String, Image> overlayRedImages = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR + "flipflop-f", FILENAMES, floorImages, ".png");
            loadImageFiles(RESOURCE_DIR + "flipflop-b", FILENAMES, overlayBlueImages, ".png");
            loadImageFiles(RESOURCE_DIR + "flipflop-r", FILENAMES, overlayRedImages, ".png");
        }
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private FlipFlopRenderResolver() {}

    /** 
     * Resolves the floor layer for a flip-flop.
     * 
     * @param flipFlop The {@link FlipFlop} instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the floor.
     */
    public static RenderState resolveFloor(
            FlipFlop flipFlop,
            GridPos pos,
            double alpha
    ) {

        EnumSet<Direction> topology = MoverTopology.resolve(flipFlop, pos);
        SpriteData sprite = SpriteSelector.flipFlop(topology, FlipFlopImage.floorImages);

        double rotation = rotationFor(flipFlop) + sprite.rotationOffset();

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

    /** 
     * Resolves the overlay layer for a flip-flop based on its active state.
     * 
     * @param flipFlop The {@link FlipFlop} instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the overlay.
     */
    public static RenderState resolveOverlay(
            FlipFlop flipFlop,
            GridPos pos,
            double alpha
    ) {

        EnumSet<Direction> topology = MoverTopology.resolve(flipFlop, pos);
        SpriteData sprite = null;
        if (flipFlop.isActive())
            sprite = SpriteSelector.flipFlop(topology, FlipFlopImage.overlayBlueImages);
        else
            sprite = SpriteSelector.flipFlop(topology, FlipFlopImage.overlayRedImages);

        double rotation = rotationFor(flipFlop) + sprite.rotationOffset();

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
