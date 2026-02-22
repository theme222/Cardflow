package ui.mover;

import component.mover.Delay;
import component.mover.RedBlackFilter;
import javafx.scene.image.Image;
import ui.render.RenderState;
import util.GridPos;

import java.util.HashMap;
import java.util.Map;

public final class DelayRenderResolver extends MoverRenderResolver {

    private static class DelayImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/delay/";
        private static final String[] FILENAMES = {"delay-base", "delay-turn"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }

    private DelayRenderResolver() {}

    public static RenderState resolve(
            Delay delay,
            GridPos pos,
            double alpha
    ) {
        MoverTopology.MoverShape topology = MoverTopology.resolve(delay, pos);

        SpriteData sprite = selectSprite(topology, DelayImage.images, "delay");

        double rotation = rotationFor(delay) + sprite.rotationOffset();

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
