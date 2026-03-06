package application.path;

import util.Direction;
import util.GridPos;

import java.util.*;

/**
 * A utility class that implements the A* pathfinding algorithm tailored for tile placement.
 * <p>
 * This pathfinder is unique because it tracks not just the position, but also the 
 * {@link Direction} used to enter a tile. This allows the pathfinding logic to 
 * handle rotation-based costs and u-turn restrictions.
 */
public class PlacementPathfinder {

    /**
     * A functional interface used to determine the cost of moving from one grid cell to another.
     */
    @FunctionalInterface
    public static interface CostFunction {
        /**
         * Calculates the cost of a specific move.
         * * @param from        The starting grid position.
         * @param to          The target grid position.
         * @param incomingDir The direction from which the 'from' node was entered.
         * @param moveDir     The direction of the current move being evaluated.
         * @return The integer cost of the move. Return a negative value to mark the move as illegal.
         */
        int movementCost(
                GridPos from,
                GridPos to,
                Direction incomingDir,
                Direction moveDir);
    }

    /**
     * A functional interface used to estimate the remaining distance to the target.
     */
    @FunctionalInterface
    public interface HeuristicFunction {
        /**
         * Estimates the cost to reach the target from the current position.
         * * @param from        The current grid position.
         * @param target      The ultimate destination.
         * @param incomingDir The direction the current node was entered from.
         * @param lastDir     The previous direction in the path sequence.
         * @return An estimated cost (usually Manhattan distance plus any tactical bias).
         */
        int estimate(
                GridPos from,
                GridPos target,
                Direction incomingDir,
                Direction lastDir);
    }

    /**
     * Represents a single point in the calculated path, consisting of a position 
     * and the direction the mover is facing at that position.
     */
    public static class PathNode {
        public final GridPos pos;
        public final Direction dir;

        public PathNode(GridPos pos, Direction dir) {
            this.pos = pos;
            this.dir = dir;
        }
    }

    /**
     * Internal record representing an A* node in the priority queue.
     */
    private record Node(
            GridPos pos,
            Direction incomingDir,
            Direction lastDir,
            int gCost, // Actual cost from start to this node
            int fCost) { // Total estimated cost (gCost + heuristic)
    }

    /** * Finds the optimal path between two points using the A* algorithm.
     * * @param start             The starting grid position.
     * @param target            The destination grid position.
     * @param finalRotation     The direction the mover should face upon reaching the target.
     * @param costFunction      Logic to determine if a move is valid and its weight.
     * @param heuristicFunction Logic to guide the search toward the target.
     * @return A {@link List} of {@link PathNode}s representing the path from start to target. 
     * Returns an empty list if no path is found.
     */
    public static List<PathNode> findPath(
            GridPos start,
            GridPos target,
            Direction finalRotation,
            CostFunction costFunction,
            HeuristicFunction heuristicFunction) {

        if (start == null || target == null)
            return Collections.emptyList();

        // Priority queue sorted by the lowest estimated total cost (fCost)
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));

        // Maps to track the best known cost to a state and the parent state for path reconstruction
        Map<State, Integer> gScore = new HashMap<>();
        Map<State, State> cameFrom = new HashMap<>();

        State startState = new State(start, null, null);
        gScore.put(startState, 0);

        open.add(new Node(
                start,
                null,
                null,
                0,
                heuristicFunction.estimate(start, target, null, null)));

        while (!open.isEmpty()) {
            Node current = open.poll();
            State currentState = new State(current.pos, current.incomingDir, current.lastDir);

            // If we reached the goal, reconstruct the path and return
            if (current.pos.equals(target))
                return reconstructPath(
                        cameFrom,
                        currentState,
                        finalRotation);

            // Explore neighbors in all 4 directions
            for (Direction dir : Direction.values()) {
                GridPos next = current.pos.addDirection(dir);

                int moveCost = costFunction.movementCost(
                        current.pos,
                        next,
                        current.incomingDir,
                        dir);

                if (moveCost < 0)
                    continue; // illegal move according to cost function

                State nextState = new State(next, dir, current.incomingDir);
                int tentativeG = gScore.get(currentState) + moveCost;

                // If this path to the neighbor is better than any previous one, record it
                if (tentativeG < gScore.getOrDefault(nextState, Integer.MAX_VALUE)) {
                    cameFrom.put(nextState, currentState);
                    gScore.put(nextState, tentativeG);

                    int f = tentativeG + heuristicFunction.estimate(
                            next,
                            target,
                            dir,
                            current.incomingDir);

                    open.add(new Node(
                            next,
                            dir,
                            current.incomingDir,
                            tentativeG,
                            f));
                }
            }
        }

        return Collections.emptyList();
    }

    /** * Backtracks through the {@code cameFrom} map to build the final list of path nodes.
     * * @param cameFrom      Map of child states to parent states.
     * @param current       The final state (target).
     * @param finalRotation The rotation to apply to the last node in the path.
     * @return An ordered list of nodes from start to finish.
     */
    private static List<PathNode> reconstructPath(
            Map<State, State> cameFrom,
            State current,
            Direction finalRotation) {

        List<State> reverse = new ArrayList<>();
        reverse.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            reverse.add(current);
        }

        Collections.reverse(reverse);

        List<PathNode> result = new ArrayList<>();
        for (int i = 0; i < reverse.size(); i++) {
            Direction dir;

            // The last node uses the user's selected rotation; 
            // others face the next node in the sequence.
            if (i == reverse.size() - 1) {
                dir = finalRotation;
            } else {
                GridPos a = reverse.get(i).pos;
                GridPos b = reverse.get(i + 1).pos;
                dir = a.directionTo(b);
            }

            result.add(new PathNode(reverse.get(i).pos, dir));
        }

        return result;
    }

    /**
     * Represents the unique identity of a pathfinding state. 
     * A state is defined by its position and its entry/previous directions.
     */
    private record State(
            GridPos pos,
            Direction incomingDir,
            Direction lastDir) {
    }
}