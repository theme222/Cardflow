package ui.mover;

import component.mover.Conveyor;
import ui.card.CardRenderResolver;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.Direction;
import util.GridPos;

import javafx.scene.image.Image;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the visual state of a {@link Conveyor} mover.
 */
public final class ConveyorRenderResolver extends MoverRenderResolver {

    /**
     * Inner class for loading conveyor images.
     */
    private static class ConveyorImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/conveyor/";
        private static final String[] FILENAMES = {"-base", "-turn", "-merge-a", "-merge-b", "-merge-c"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR + "conveyor", FILENAMES, images, ".png");
        }
    }


    /**
     * Private constructor to prevent instantiation.
     */
    private ConveyorRenderResolver() {}

    /** 
     * Resolves the render state for a conveyor.
     * 
     * @param conveyor The conveyor instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the conveyor.
     */
    public static RenderState resolve(
            Conveyor conveyor,
            GridPos pos,
            double alpha
    ) {
        EnumSet<Direction> topology = MoverTopology.resolve(conveyor, pos);
        SpriteData sprite = SpriteSelector.regular(topology, ConveyorImage.images);
        double rotation = rotationFor(conveyor) + sprite.rotationOffset();

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
