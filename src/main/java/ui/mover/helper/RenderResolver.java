package ui.mover.helper;

import component.mover.Conveyor;
import component.mover.Mover;
import javafx.scene.image.Image;

public abstract class RenderResolver {

    protected static double rotationFor(Mover mover) {
        return switch (mover.getDirection()) {
            case UP -> 0;
            case RIGHT -> 90;
            case DOWN -> 180;
            case LEFT -> 270;
            default -> 0;
        };
    }

    public record SpriteData(
            Image image,
            double rotationOffset,
            boolean mirrorX
    ) {}


}
