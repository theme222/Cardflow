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

public final class ConveyorRenderResolver extends MoverRenderResolver {

    private static class ConveyorImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/conveyor/";
        private static final String[] FILENAMES = {"-base", "-turn", "-merge-a", "-merge-b", "-merge-c"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR + "conveyor", FILENAMES, images, ".png");
        }
    }


    private ConveyorRenderResolver() {}

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
                alpha
        );
    }


}
