package ui.mover;

import component.mover.Conveyor;
import component.mover.FlipFlop;
import component.mover.ParityFilter;
import javafx.scene.image.Image;
import ui.mover.helper.MoverTopology;
import ui.mover.helper.MoverTopology.MoverShape;
import ui.mover.helper.RenderResolver;
import ui.render.RenderState;
import util.GridPos;

public final class ParityFilterRenderResolver extends RenderResolver {

    // TODO ADD ACTUAL IMAGES TO THIS
    private static final Image BASE_IMAGE = new Image(
            ParityFilterRenderResolver.class.getResourceAsStream(
                    "/asset/tiles/mover/conveyor/conveyor-base.png"),
            0, 0, true, false);

    private static final Image TURN_RIGHT_IMAGE = new Image(
            ParityFilterRenderResolver.class.getResourceAsStream(
                    "/asset/tiles/mover/conveyor/conveyor-turn-right.png"),
            0, 0, true, false);

    private ParityFilterRenderResolver() {}

    public static RenderState resolve(
            ParityFilter parityFilter,
            GridPos pos,
            double alpha
    ) {
        MoverShape topology = MoverTopology.resolve(parityFilter, pos);

        SpriteData sprite = selectSprite(topology);

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

    private static SpriteData selectSprite( MoverShape topology ) {
        return switch (topology) {
            case TURN_RIGHT ->
                new SpriteData(TURN_RIGHT_IMAGE, -90, false);

            case TURN_LEFT ->
                new SpriteData(TURN_RIGHT_IMAGE, +90, true);

            default ->
                new SpriteData(BASE_IMAGE, 0, false);
        };
    }

}
