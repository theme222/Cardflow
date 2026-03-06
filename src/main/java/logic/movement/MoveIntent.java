package logic.movement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import component.card.Card;
import component.modifier.Modifier;
import component.mover.Mover;
import logic.GameLevel;
import util.Direction;
import util.GridPos;

class MoveIntent {
    // TODO: DEBUG THIS FULLY AND MAKE IT BE COMPLIANT WITH GETTERS AND SETTERS

    static final HashMap<GridPos, MoveIntent> byCurrent = new HashMap<>();
    static final HashMap<GridPos, ArrayList<MoveIntent>> byResult = new HashMap<>();

    enum IntentStatus {
        UNRESOLVED,
        MOVED,
        BLOCKED
    }

    Card card;
    Direction direction;
    IntentStatus status;

    public MoveIntent(Card c, Direction d) {
        card = c;
        direction = d;
        status = direction == Direction.STAY ? IntentStatus.BLOCKED : IntentStatus.UNRESOLVED;
    }

    public GridPos getResultPos() { // Returns new point
        GridPos t = Mover.getTranslationFromDirection(direction);
        GridPos resultPos = new GridPos(card.getGridPos());
        return resultPos.add(t);
    }

    @Override
    public String toString() {
        return "MoveIntent{" +
                "card=" + card +
                ", direction=" + direction +
                ", status=" + status +
                '}';
    }

    public GridPos getCurrentPos() { // Returns new point
        return new GridPos(card.getGridPos());
    }

    private static ArrayList<GridPos> getSurroundingPoints(GridPos resultPos) {
        // If somebody has a better way of doing this please fix thx
        ArrayList<GridPos> surroundPos = new ArrayList<>();

        GridPos topPoint = resultPos.add(0, -1);
        GridPos leftPoint = resultPos.add(-1, 0);
        GridPos rightPoint = resultPos.add(1, 0);
        GridPos bottomPoint = resultPos.add(0, 1);

        surroundPos.add(topPoint);
        surroundPos.add(leftPoint);
        surroundPos.add(rightPoint);
        surroundPos.add(bottomPoint);
        return surroundPos;
    }

    public static void resolveIntent(List<MoveIntent> intentList, HashSet<GridPos> seen) { // linear recursive function
                                                                                           // (we hate recursion)
                                                                                           // just use stack bro
        // intentList is a variable that should contain intents that have the same
        // result position.
        if (intentList.isEmpty() || intentList.size() > 4)
            throw new IndexOutOfBoundsException("No move intents found");

        // intentList should have [1,4] members (top left right bottom)

        // Check where the intent is trying to go
        GridPos resultPos = intentList.getFirst().getResultPos();

        // Get the priority intent (Top -> Left -> Right -> Bottom)
        MoveIntent priorityIntent = null;
        ArrayList<GridPos> surroundPos = getSurroundingPoints(resultPos);

        for (GridPos p : surroundPos) {
            if (priorityIntent != null)
                break;
            for (MoveIntent intent : intentList) {
                if (intent.getCurrentPos().equals(p)) {
                    priorityIntent = intent;
                    break;
                }
            }
        }

        // Block all intents that aren't the priority
        for (MoveIntent intent : intentList) {
            if (intent != priorityIntent)
                intent.status = IntentStatus.BLOCKED;
        }

        if (priorityIntent == null)
            throw new RuntimeException("No move intents found ?????");

        // From here on we only care about the priority intent

        // Check within range
        if (!resultPos.inRange(0, GameLevel.getInstance().WIDTH - 1, 0, GameLevel.getInstance().HEIGHT - 1)) {
            priorityIntent.status = IntentStatus.BLOCKED;
            return;
        }

        // Check position contains Modifier / Mover that is blocking.
        Modifier resultPosMod = GameLevel.getInstance().getTile(resultPos).getModifier();
        Mover resultPosMove = GameLevel.getInstance().getTile(resultPos).getMover();

        if ((resultPosMod != null && resultPosMod.isBlocking()) ||
                (resultPosMove != null && resultPosMove.isBlocking())) {
            // Technically currently resultPosMove.isBlocking() will never return true but
            // ya never know what might happen
            priorityIntent.status = IntentStatus.BLOCKED;
            return;
        }

        // Check position has a card or doesn't
        if (GameLevel.getInstance().getTile(resultPos).getCard() != null) {
            // Resolve the next position
            MoveIntent blockingCardIntent = byCurrent.get(resultPos); // get MUST NOT ERROR OR BE NULL HERE

            if (seen.contains(resultPos)) { // We have evaluated this position before (implies cycle)
                // Checking whether cards are facing each other [C1 -> <- C2]
                // Check whether getResultPos() equals to any card in the intentList
                GridPos blockingCardResultPos = blockingCardIntent.getResultPos();

                boolean isEqual = false;
                for (MoveIntent intent : intentList)
                    if (intent.getCurrentPos().equals(blockingCardResultPos))
                        isEqual = true;

                if (isEqual)
                    priorityIntent.status = IntentStatus.BLOCKED;
                else
                    priorityIntent.status = IntentStatus.MOVED;
                return;
            }

            for (MoveIntent intent : intentList) // Add all intents to seen
                seen.add(intent.getCurrentPos());

            if (blockingCardIntent.status == IntentStatus.BLOCKED) { // If the position we are going to has a card that
                                                                     // is blocked
                priorityIntent.status = IntentStatus.BLOCKED;
                return;
            } else if (blockingCardIntent.status == IntentStatus.MOVED) { // If the position we are going to has a card
                                                                          // that has moved
                priorityIntent.status = IntentStatus.MOVED;
                return;
            }

            // the blocking card contains intent that is unresolved. we call resolve intent
            // to collapse it
            List<MoveIntent> resultIntentList = byResult.get(blockingCardIntent.getResultPos());
            resolveIntent(resultIntentList, seen);

            // Re check one more time lol
            if (blockingCardIntent.status == IntentStatus.BLOCKED) { // If the position we are going to has a card that
                                                                     // is blocked
                priorityIntent.status = IntentStatus.BLOCKED;
                return;
            } else if (blockingCardIntent.status == IntentStatus.MOVED) { // If the position we are going to has a card
                                                                          // that has moved
                priorityIntent.status = IntentStatus.MOVED;
                return;
            } else
                throw new RuntimeException("What the actual hell happened here?");
        } else {
            priorityIntent.status = IntentStatus.MOVED;
            return;
        }
    }
}