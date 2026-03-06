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

/**
 * Abstract base class for mover render resolvers.
 * Provides shared logic for calculating rotations, selecting sprites based on topology,
 * and resolving adjacent connections.
 */
public abstract class MoverRenderResolver extends RenderResolver {

    /** 
     * Returns the base rotation angle for a mover based on its logical rotation.
     * 
     * @param mover The mover instance.
     * @return The rotation angle in degrees.
     */
    public static double rotationFor(Mover mover) {
        return switch (mover.getRotation()) {
            case UP -> 0;
            case RIGHT -> 90;
            case DOWN -> 180;
            case LEFT -> 270;
            default -> 0;
        };
    }

    /**
     * Record holding sprite image and transformation data.
     * 
     * @param image The image to use.
     * @param rotationOffset Additional rotation offset for the sprite.
     * @param mirrorX Whether to flip the sprite horizontally.
     */
    public record SpriteData(
            Image image,
            double rotationOffset,
            boolean mirrorX
    ) {}

    /**
     * Utility for selecting the correct sprite based on adjacent mover connections.
     */
    public static final class SpriteSelector {
        /**
         * Selects a sprite for standard movers like conveyors and delays.
         * 
         * @param topology The set of directions from which other movers are facing this one.
         * @param images The map of available sprite images.
         * @return The selected {@link SpriteData}.
         */
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

        /**
         * Selects a sprite for flip-flop movers.
         * 
         * @param topology The connected directions.
         * @param images The available images.
         * @return The selected {@link SpriteData}.
         */
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

        /**
         * Selects a sprite for filter movers.
         * 
         * @param topology The connected directions.
         * @param images The available images.
         * @return The selected {@link SpriteData}.
         */
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

    /**
     * Utility for resolving the local topology (connections) of a mover on the grid.
     */
    public static final class MoverTopology {

        /** 
         * Determines which adjacent movers are facing into the specified mover.
         * 
         * @param conveyor The mover to check connections for.
         * @param pos Its grid position.
         * @return An {@link EnumSet} of {@link Direction}s indicating incoming connections relative to the mover's facing.
         */
        public static EnumSet<Direction> resolve(Mover conveyor, GridPos pos) {
            GameTile[] tiles = GameLevel.getInstance().getAdjacentTiles(pos);
            Direction forward = conveyor.getRotation();

            EnumSet<Direction> moverInputs = EnumSet.noneOf(Direction.class);

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
