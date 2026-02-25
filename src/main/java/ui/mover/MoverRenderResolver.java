package ui.mover;

import component.GameTile;
import component.mover.Conveyor;
import component.mover.Mover;
import javafx.scene.image.Image;
import logic.GameLevel;
import ui.render.RenderResolver;
import util.Direction;
import util.GridPos;

import java.util.EnumSet;
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

    public static final class SpriteSelector {
        // I know this looks bad but I promise you its not really that bad
        // DOWN = BEHIND, UP = AHEAD
        public static SpriteData regular(EnumSet<Direction> topology, Map<String, Image> images) {
            // Conveyor + Delay
            // UP doesn't do anything
            if (topology.containsAll(EnumSet.of(Direction.DOWN,  Direction.LEFT, Direction.RIGHT)))
                return new SpriteData(images.get("-merge-c"), 0, false);
            else if (topology.containsAll(EnumSet.of(Direction.LEFT,  Direction.RIGHT)))
                return new SpriteData(images.get("-merge-a"), 0, false);
            else if (topology.containsAll(EnumSet.of(Direction.RIGHT,  Direction.DOWN)))
                return new SpriteData(images.get("-merge-b"), 0, false);
            else if (topology.containsAll(EnumSet.of(Direction.LEFT,  Direction.DOWN)))
                return new SpriteData(images.get("-merge-b"), 0, true);
            else if (topology.contains(Direction.LEFT))
                return new SpriteData(images.get("-turn"), 0, true);
            else if (topology.contains(Direction.RIGHT))
                return new SpriteData(images.get("-turn"), 0, false);
            else
                return new SpriteData(images.get("-base"), 0, false);
        }

        public static SpriteData flipFlop(EnumSet<Direction> topology, Map<String, Image> images) {
            // FlipFlop
            // DOWN and UP doesn't do anything
            if (topology.containsAll(EnumSet.of(Direction.LEFT, Direction.RIGHT)))
                return new SpriteData(images.get("-merge-c"), 0, false);
            else if (topology.contains(Direction.RIGHT))
                return new SpriteData(images.get("-merge-a"), 0, true);
            else if (topology.contains(Direction.LEFT))
                return new SpriteData(images.get("-merge-a"), 0, false);
            else
                return new SpriteData(images.get("-base"), 0, false);
        }

        public static SpriteData filter(EnumSet<Direction> topology, Map<String, Image> images) {
            // ParityFilter + RedBlackFilter
            // UP AND LEFT doesn't do anything
            if (topology.containsAll(EnumSet.of(Direction.DOWN, Direction.RIGHT)))
                return new SpriteData(images.get("-merge-c"), 0, false);
            else if (topology.contains(Direction.DOWN))
                return new SpriteData(images.get("-merge-a"), 0, false);
            else if (topology.contains(Direction.RIGHT))
                return new SpriteData(images.get("-merge-b"), 0, false);
            else
                return new SpriteData(images.get("-base"), 0, false);
        }

    }

    public static final class MoverTopology {

        // We will use enum set instead (EnumSet<Direction>)

        public static EnumSet<Direction> resolve(Mover conveyor, GridPos pos) {
            GameTile[] tiles = GameLevel.getInstance().getAdjacentTiles(pos);
            Direction forward = conveyor.getRotation();

            EnumSet<Direction> moverInputs = EnumSet.noneOf(Direction.class);

            System.out.println("Resolving topology for conveyor at " + pos +
                    " facing " + forward);

            for (int i = 0; i < tiles.length; i++) {
                GameTile tile = tiles[i];
                if (tile == null || tile.getMover() == null) continue;

                Mover adjacentMover = tile.getMover();

                Direction[] possibleAdjDirections = adjacentMover.getValidOutputDirections();
                Direction adjacentDirection = null;

                // check if facing into
                for (Direction direction : possibleAdjDirections) {
                    if(tile.getGridPos().addDirection(direction).equals(pos)) {
                        adjacentDirection = direction;
                        break;
                    }
                }

                if (adjacentDirection == null) continue;

                if (forward.equals(adjacentDirection)) {
                    moverInputs.add(Direction.DOWN); // BEHIND
                } else if (forward.isLeftOf(adjacentDirection)) {
                    moverInputs.add(Direction.LEFT);
                } else if (forward.isRightOf(adjacentDirection)) {
                    moverInputs.add(Direction.RIGHT);
                } else if (forward.isOpposite(adjacentDirection)) {
                    moverInputs.add(Direction.UP); // AHEAD
                }
            }

            return moverInputs;
        }
    }

}
