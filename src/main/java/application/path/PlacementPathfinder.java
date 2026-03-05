package application.path;

import util.Direction;
import util.GridPos;

import java.util.*;

public class PlacementPathfinder {

    @FunctionalInterface
    public static interface CostFunction {
        int movementCost(
                GridPos from,
                GridPos to,
                Direction incomingDir,
                Direction moveDir);
    }

    @FunctionalInterface
    public interface HeuristicFunction {
        int estimate(
                GridPos from,
                GridPos target,
                Direction incomingDir,
                Direction lastDir);
    }

    public static class PathNode {
        public final GridPos pos;
        public final Direction dir;

        public PathNode(GridPos pos, Direction dir) {
            this.pos = pos;
            this.dir = dir;
        }
    }

    private record Node(
            GridPos pos,
            Direction incomingDir,
            Direction lastDir,
            int gCost,
            int fCost) {
    }

    public static List<PathNode> findPath(
            GridPos start,
            GridPos target,
            Direction finalRotation,
            CostFunction costFunction,
            HeuristicFunction heuristicFunction) {

        if (start == null || target == null)
            return Collections.emptyList();

        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt(n -> n.fCost));

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

            if (current.pos.equals(target))
                return reconstructPath(
                        cameFrom,
                        currentState,
                        finalRotation);

            for (Direction dir : Direction.values()) {

                GridPos next = current.pos.addDirection(dir);

                int moveCost = costFunction.movementCost(
                        current.pos,
                        next,
                        current.incomingDir,
                        dir);

                if (moveCost < 0)
                    continue; // negative means illegal move

                State nextState = new State(next, dir, current.incomingDir);

                int tentativeG = gScore.get(currentState) + moveCost;

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

    private record State(
            GridPos pos,
            Direction incomingDir,
            Direction lastDir) {
    }
}