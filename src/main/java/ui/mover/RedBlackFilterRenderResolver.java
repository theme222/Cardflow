package ui.mover;

import component.mover.RedBlackFilter;
import javafx.scene.image.Image;
import ui.render.RenderState;
import util.GridPos;

import java.util.HashMap;
import java.util.Map;

public final class RedBlackFilterRenderResolver extends MoverRenderResolver {

    // TODO ADD ACTUAL IMAGES TO THIS
    private static class RedBlackFilterImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/flipflop/";
        private static final String[] FILENAMES = {"flipflop-base", "flipflop-turn"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }

    private RedBlackFilterRenderResolver() {}

    public static RenderState resolve(
            RedBlackFilter redBlackFilter,
            GridPos pos,
            double alpha
    ) {
        MoverTopology.MoverShape topology = MoverTopology.resolve(redBlackFilter, pos);

        SpriteData sprite = selectSprite(topology, RedBlackFilterImage.images, "flipflop");

        double rotation = rotationFor(redBlackFilter) + sprite.rotationOffset();

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
