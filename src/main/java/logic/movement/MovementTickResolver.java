package logic.movement;

import java.util.ArrayList;
import java.util.HashSet;

import component.card.Card;
import component.mover.Mover;
import logic.GameLevel;
import util.Direction;
import util.GridPos;

public class MovementTickResolver {
    static public HashSet<CardMovement> doMovementTick(GameLevel gameLevel, HashSet<Card> cardSet, HashSet<GridPos> changedPoints) {
        // If you are reading this I am so sorry on what you are about to witness
        // Also theoretically O(n) but honestly I have no idea if it actually is
        System.out.println("DOING MOVEMENT TICK"); // TODO: DEBUG

        changedPoints.clear();

        // Clear static variables to remove previous run info
        MoveIntent.byCurrent.clear();
        MoveIntent.byResult.clear();

        for (Card card : cardSet) {
            // Get direction from the mover under the card
            Mover mover = gameLevel.getTile(card.getGridPos()).getMover();
            Direction direction = mover == null ? Direction.STAY : mover.getDirection();

            // Calculate and fill up the intent hashmap for later access
            MoveIntent intent = new MoveIntent(card, direction);

            MoveIntent.byCurrent.put(intent.getCurrentPos(), intent);

            if (!MoveIntent.byResult.containsKey(intent.getResultPos()))
                MoveIntent.byResult.put(intent.getResultPos(), new ArrayList<>());

            MoveIntent.byResult.get(intent.getResultPos()).add(intent);
        }

        System.out.println(MoveIntent.byCurrent);
        System.out.println(MoveIntent.byResult);

        // Loop through intents and resolve them
        for (MoveIntent intent : MoveIntent.byCurrent.values())
            if (intent.status == MoveIntent.IntentStatus.UNRESOLVED)
                MoveIntent.resolveIntent(MoveIntent.byResult.get(intent.getResultPos()), new HashSet<>());

        HashSet<CardMovement> movements = new HashSet<>();

        for (MoveIntent intent : MoveIntent.byCurrent.values()) { // Commit all intents
            if (intent.status == MoveIntent.IntentStatus.MOVED) {
                if (gameLevel.getTile(intent.getCurrentPos()).getCard() == intent.card) // Remove old position if not
                                                                                        // yet overriden
                    gameLevel.getTile(intent.getCurrentPos()).removeSameClassOnTile(intent.card);

                changedPoints.add(intent.getCurrentPos());
                changedPoints.add(intent.getResultPos());
                System.out.println("Moved " + intent.card + " from " + intent.getCurrentPos() + " to " + intent.getResultPos()); // TODO: DEBUG
                movements.add(new CardMovement(intent.card, intent.getCurrentPos(), intent.getResultPos()));
                
                gameLevel.setPositionOnGrid(intent.card, intent.getResultPos(), true); // THIS IS MUTABLE AND CAN CAUSE PROBLEMS IF YOU REF IT AFTER
                
            } else if (intent.status == MoveIntent.IntentStatus.UNRESOLVED) {
                throw new RuntimeException("UHHHHHHHHHHHHHHHH HOW??????");
            }
        }
        System.out.println("DONE MOVEMENT TICK"); // TODO: DEBUG
        return movements;
    }
}
