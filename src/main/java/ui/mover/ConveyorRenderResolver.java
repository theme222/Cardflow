package ui.mover;

import component.mover.Conveyor;
import ui.card.CardRenderResolver;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.GridPos;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public final class ConveyorRenderResolver extends MoverRenderResolver {

    private static class ConveyorImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/conveyor/";
        private static final String[] FILENAMES = {"conveyor-base", "conveyor-turn"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }


    private ConveyorRenderResolver() {}

    public static RenderState resolve(
            Conveyor conveyor,
            GridPos pos,
            double alpha
    ) {
        MoverTopology.MoverShape topology = MoverTopology.resolve(conveyor, pos);
        SpriteData sprite = selectSprite(topology, ConveyorImage.images, "conveyor");
        double rotation = rotationFor(conveyor) + sprite.rotationOffset();

        return new RenderState(
                sprite.image(),
                85,
                85,
                rotation,
                sprite.mirrorX(),
                alpha
        );
    }


}
