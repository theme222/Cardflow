package application.controller;

import java.util.ArrayList;
import java.util.List;

import application.path.PlacementPathfinder;
import logic.GameLevel;
import util.Direction;
import util.GridPos;

/**
 * Utility class responsible for constructing a path of {@link PlacementNode}s 
 * between two points on the grid.
 * <p>
 * It utilizes a pathfinding algorithm with custom cost and heuristic functions 
 * to determine the most logical placement route based on current game constraints 
 * (e.g., whether the user is placing or deleting tiles).
 */
public class PlacementPathBuilder {

    /**
     * Represents a single step in a placement path, containing position, 
     * direction, and intent (placement vs deletion).
     */
    public static class PlacementNode {
        private GridPos pos;
        private Direction dir;
        private boolean delete;

        /** @param pos The grid coordinate for this node. */
        public void setPos(GridPos pos) {this.pos = pos;}
        
        /** @param dir The orientation of the mover at this node. */
        public void setDir(Direction dir) {this.dir = dir;}
        
        /** @param delete {@code true} if this node represents a removal action. */
        public void setDel(boolean delete) {this.delete = delete;}
        
        /** @return The grid coordinate. */
        public GridPos getPos() {return this.pos;}
        
        /** @return The orientation direction. */
        public Direction getDir() {return this.dir;}
        
        /** @return {@code true} if deleting, {@code false} if placing. */
        public boolean getDel() {return this.delete;}
    }

    /** * Builds a list of placement nodes from a start to an end position.
     * <p>
     * The pathfinding logic shifts based on the starting tile:
     * <ul>
     * <li>If the start tile is occupied, it enters <b>Delete Mode</b>.</li>
     * <li>If the start tile is empty, it enters <b>Placement Mode</b>.</li>
     * </ul>
     * * @param start    The origin coordinates of the path.
     * @param end      The destination coordinates (usually current mouse pos).
     * @param rotation The initial/intended rotation of the movers.
     * @return A list of {@link PlacementNode} objects representing the calculated path.
     */
    public static List<PlacementNode> buildPath(
        GridPos start,
        GridPos end,
        Direction rotation
    ) {

        ArrayList<PlacementNode> placementList = new ArrayList<>();

        if (start == null || end == null)
            return placementList;

        GameLevel level = GameLevel.getInstance();

        // Determine mode: If we start on a mover, we are dragging to delete.
        boolean deleteMode = level.getTile(start).getMover() != null;

        /**
         * Defines movement rules:
         * 1. Prevents moving out of bounds.
         * 2. Matches the "deleteMode" (can't place on occupied, can't delete empty).
         * 3. Prevents 180-degree U-turns.
         * 4. Adds a penalty for turns to encourage straight lines.
         */
        PlacementPathfinder.CostFunction costFunction = (from, to, incomingDir, moveDir) -> {

            if (!level.isInBounds(to))
                return -1; // illegal

            boolean occupied = level.getTile(to).getMover() != null;

            // Mode validation
            if (deleteMode) {
                if (!occupied) return -1;
            } else {
                if (occupied) return -1;
            }

            int cost = 1; // base movement

            // 180° block (preventing jittery back-and-forth)
            if (incomingDir != null && moveDir == incomingDir.opposite())
                return -1;

            // Turn penalty (prefer straight paths)
            if (incomingDir != null && moveDir != incomingDir)
                cost += 2;

            return cost;
        };

        /**
         * Estimates the remaining cost to the target.
         * Includes a tactical bias based on the current rotation to make the 
         * pathing feel "natural" relative to how the movers face.
         */
        PlacementPathfinder.HeuristicFunction heuristic =
        (from, target, incomingDir, lastDir) -> {

            int dx = target.getX() - from.getX();
            int dy = target.getY() - from.getY();

            int manhattan = Math.abs(dx) + Math.abs(dy);

            if (incomingDir == null) {
                return manhattan;
            }

            // --- Tactical bias ---
            boolean inFront = target.isPosInDirection(from, rotation);
            boolean behind  = target.isPosInDirection(from, rotation.opposite());

            int bias = 0;

            if (inFront) {
                // Prefer sideways first to align for a straight shot
                if (!rotation.isOpposite(incomingDir)) {
                    bias += manhattan;
                }
            } else if (behind) {
                // Prefer aligning the tail
                if (!rotation.isPerpendicularOf(incomingDir)) {
                    bias += manhattan;
                }
            }

            return manhattan + bias;
        };

        // Execute the pathfinding algorithm
        var path = PlacementPathfinder.findPath(
                start,
                end,
                rotation,
                costFunction,
                heuristic);

        // Convert the generic path results into PlacementNodes
        for (var p : path) {
            PlacementNode node = new PlacementNode();
            node.setPos(p.pos);
            node.setDir(p.dir);
            node.setDel(deleteMode);
            placementList.add(node);
        }

        return placementList;
    }
}