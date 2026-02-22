package ui.mover;

import component.mover.ParityFilter;
import javafx.scene.image.Image;
import ui.render.RenderState;
import util.GridPos;

import java.util.HashMap;
import java.util.Map;

public final class ParityFilterRenderResolver extends MoverRenderResolver {

    // TODO ADD ACTUAL IMAGES TO THIS
    private static class ParityFilterImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/flipflop/";
        private static final String[] FILENAMES = {"flipflop-base", "flipflop-turn"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }

    private ParityFilterRenderResolver() {}

    public static RenderState resolve(
            ParityFilter parityFilter,
            GridPos pos,
            double alpha
    ) {
        MoverTopology.MoverShape topology = MoverTopology.resolve(parityFilter, pos);

        SpriteData sprite = selectSprite(topology, ParityFilterImage.images, "flipflop");

        double rotation = rotationFor(parityFilter) + sprite.rotationOffset();

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
