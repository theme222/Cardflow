package ui.mover;

import component.mover.FlipFlop;
import javafx.scene.image.Image;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.GridPos;

import java.util.HashMap;
import java.util.Map;

public final class FlipFlopRenderResolver extends MoverRenderResolver {

    private static class FlipFlopImage {
        private static final String RESOURCE_DIR = "/asset/tiles/mover/flipflop/";
        private static final String[] FILENAMES = {"flipflop-base", "flipflop-turn"};
        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }

    private FlipFlopRenderResolver() {}

    public static RenderState resolve(
            FlipFlop flipFlop,
            GridPos pos,
            double alpha
    ) {
        MoverTopology.MoverShape topology = MoverTopology.resolve(flipFlop, pos);

        SpriteData sprite = selectSprite(topology, FlipFlopImage.images, "flipflop");

        double rotation = rotationFor(flipFlop) + sprite.rotationOffset();

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
