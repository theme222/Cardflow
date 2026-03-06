package application.controller;

import java.util.ArrayList;
import java.util.List;

import application.path.PlacementPathfinder;
import logic.GameLevel;
import util.Direction;
import util.GridPos;

public class PlacementPathBuilder {

    public static class PlacementNode {
        private GridPos pos;
        private Direction dir;
        private boolean delete;
        public void setPos(GridPos pos) {this.pos = pos;}
        public void setDir(Direction dir) {this.dir = dir;}
        public void setDel(boolean delete) {this.delete = delete;}
        public GridPos getPos() {return this.pos;}
        public Direction getDir() {return this.dir;}
        public boolean getDel() {return this.delete;}
    }

    public static List<PlacementNode> buildPath(
        GridPos start,
        GridPos end,
        Direction rotation
    ) {

        ArrayList<PlacementNode> placementList = new ArrayList<>();

        if (start == null || end == null)
            return placementList;

        GameLevel level = GameLevel.getInstance();

        boolean deleteMode = level.getTile(start).getMover() != null;

        // --- COST FUNCTION ---
        PlacementPathfinder.CostFunction costFunction = (from, to, incomingDir, moveDir) -> {

            if (!level.isInBounds(to))
                return -1; // illegal

            boolean occupied = level.getTile(to).getMover() != null;

            // Delete mode: only allow deleting occupied tiles
            if (deleteMode) {
                if (!occupied)
                    return -1;
            } else {
                if (occupied)
                    return -1;
            }

            int cost = 1; // base movement

            // 180° block
            if (incomingDir != null &&
                    moveDir == incomingDir.opposite())
                return -1;

            // Turn penalty
            if (incomingDir != null &&
                    moveDir != incomingDir)
                cost += 2;

            return cost;
        };

        // --- HEURISTIC FUNCTION ---
        PlacementPathfinder.HeuristicFunction heuristic =
        (from, target, incomingDir, lastDir) -> {

            int dx = target.getX() - from.getX();
            int dy = target.getY() - from.getY();

            int manhattan = Math.abs(dx) + Math.abs(dy);

            // If still null (no facing info), fallback
            if (incomingDir == null) {
                return manhattan;
            }

            // --- Tactical bias ---
            boolean inFront = target.isPosInDirection(from, rotation);
            boolean behind  = target.isPosInDirection(from, rotation.opposite());

            int bias = 0;

            if (inFront) {
                // Prefer sideways first
                if (!rotation.isOpposite(incomingDir)) {
                    bias += manhattan;
                }
            } else if (behind) {
                // Prefer aligning
                if (!rotation.isPerpendicularOf(incomingDir)) {
                    bias += manhattan;
                }
            }

            return manhattan + bias;
        };

        var path = PlacementPathfinder.findPath(
                start,
                end,
                rotation,
                costFunction,
                heuristic);

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