package ui.mover;

import component.GameTile;
import component.mover.Conveyor;
import component.mover.Mover;
import javafx.scene.image.Image;
import logic.GameLevel;
import ui.render.RenderResolver;
import util.Direction;
import util.GridPos;

import java.util.Map;

public abstract class MoverRenderResolver extends RenderResolver {

    public static double rotationFor(Mover mover) {
        return switch (mover.getRotation()) {
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

    public static SpriteData selectSprite(MoverTopology.MoverShape topology, Map<String, Image> images, String className) {
        return switch (topology) {
            case TURN_RIGHT -> new SpriteData(images.get(className + "-turn"), -90, false);
            case TURN_LEFT -> new SpriteData(images.get(className + "-turn"), +90, true);
            default -> new SpriteData(images.get(className + "-base"), 0, false);
        };
    }


    public static final class MoverTopology {

        public enum MoverShape {
            STRAIGHT,
            TURN_LEFT,
            TURN_RIGHT,
            MERGE_LEFT_RIGHT,
            MERGE_STRAIGHT_LEFT,
            MERGE_STRAIGHT_RIGHT,
            MERGE_ALL
        }

        public static MoverShape resolve(Mover conveyor, GridPos pos) {
            GameTile[] tiles = GameLevel.getInstance().getAdjacentTiles(pos);
            Direction forward = conveyor.getRotation();

            boolean STRAIGHT_FOUND = false;
            boolean LEFT_FOUND = false;
            boolean RIGHT_FOUND = false;

            System.out.println("Resolving topology for conveyor at " + pos +
                    " facing " + forward);

            for (int i = 0; i < tiles.length; i++) {
                GameTile tile = tiles[i];
                if (tile != null && tile.getMover() != null && tile.getMover() instanceof Conveyor) {
                    Conveyor adjacentConveyor = (Conveyor) tile.getMover();
                    Direction adjacentDirection = adjacentConveyor.getRotation();
                    // check if facing into
                    if(!tile.getGridPos().addDirection(adjacentDirection).equals(pos)) continue;

                    System.out.println("Adjacent conveyor at " + adjacentConveyor.getGridPos() +
                            " facing " + adjacentDirection +
                            " relative to conveyor at " + pos +
                            " facing " + forward);

                    if (forward.equals(adjacentDirection)) {
                        STRAIGHT_FOUND = true;
                    } else if (forward.isLeftOf(adjacentDirection)) {
                        LEFT_FOUND = true;
                    } else if (forward.isRightOf(adjacentDirection)) {
                        RIGHT_FOUND = true;
                    }
                }
            }

            System.out.println("Conveyor at " + pos +
                    " has connections - Straight: " + STRAIGHT_FOUND +
                    ", Left: " + LEFT_FOUND +
                    ", Right: " + RIGHT_FOUND);

            if (STRAIGHT_FOUND && LEFT_FOUND && RIGHT_FOUND) {
                return MoverShape.MERGE_ALL;
            } else if (STRAIGHT_FOUND && LEFT_FOUND) {
                return MoverShape.MERGE_STRAIGHT_LEFT;
            } else if (STRAIGHT_FOUND && RIGHT_FOUND) {
                return MoverShape.MERGE_STRAIGHT_RIGHT;
            } else if (LEFT_FOUND && RIGHT_FOUND) {
                return MoverShape.MERGE_LEFT_RIGHT;
            } else if (STRAIGHT_FOUND) {
                return MoverShape.STRAIGHT;
            } else if (LEFT_FOUND) {
                return MoverShape.TURN_LEFT;
            } else if (RIGHT_FOUND) {
                return MoverShape.TURN_RIGHT;
            }
            return MoverShape.STRAIGHT;
        }
    }

}
