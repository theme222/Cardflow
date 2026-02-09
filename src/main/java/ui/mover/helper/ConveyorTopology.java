package ui.mover.helper;

import component.GameTile;
import component.mover.Conveyor;
import logic.GameLevel;
import util.*;

public final class ConveyorTopology {

    public enum ConveyorShape {
        STRAIGHT,
        TURN_LEFT,
        TURN_RIGHT,
        MERGE_LEFT_RIGHT,
        MERGE_STRAIGHT_LEFT,
        MERGE_STRAIGHT_RIGHT,
        MERGE_ALL
    }

    public static ConveyorShape resolve(
            Conveyor conveyor, GridPos pos) {
        GameTile[] tiles = GameLevel.getInstance().getAdjacentTiles(pos);
        Direction forward = conveyor.getDirection();

        boolean STRAIGHT_FOUND = false;
        boolean LEFT_FOUND = false;
        boolean RIGHT_FOUND = false;

        System.out.println("Resolving topology for conveyor at " + pos +
                " facing " + forward);

        for (int i = 0; i < tiles.length; i++) {
            GameTile tile = tiles[i];
            if (tile != null && tile.getMover() != null && tile.getMover() instanceof Conveyor) {
                Conveyor adjacentConveyor = (Conveyor) tile.getMover();
                Direction adjacentDirection = adjacentConveyor.getDirection();
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
            return ConveyorShape.MERGE_ALL;
        } else if (STRAIGHT_FOUND && LEFT_FOUND) {
            return ConveyorShape.MERGE_STRAIGHT_LEFT;
        } else if (STRAIGHT_FOUND && RIGHT_FOUND) {
            return ConveyorShape.MERGE_STRAIGHT_RIGHT;
        } else if (LEFT_FOUND && RIGHT_FOUND) {
            return ConveyorShape.MERGE_LEFT_RIGHT;
        } else if (STRAIGHT_FOUND) {
            return ConveyorShape.STRAIGHT;
        } else if (LEFT_FOUND) {
            return ConveyorShape.TURN_LEFT;
        } else if (RIGHT_FOUND) {
            return ConveyorShape.TURN_RIGHT;
        }
        return ConveyorShape.STRAIGHT;
    }
}
