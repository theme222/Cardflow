package logic.event;

import java.util.HashSet;

import logic.movement.CardMovement;

/**
 * Event fired after a movement phase has been completed in a tick.
 * Carries information about all card movements that occurred.
 */
public class AfterMovementEvent extends LogicEvent {
    /**
     * The set of card movements that were executed.
     */
    final private HashSet<CardMovement> movements;

    /**
     * Constructs a new AfterMovementEvent.
     * 
     * @param movements A set of {@link CardMovement} objects.
     */
    public AfterMovementEvent(HashSet<CardMovement> movements) {
        super();
        this.movements = movements;
    }
    
    /** 
     * Gets the set of movements that occurred.
     * 
     * @return A {@link HashSet} of {@link CardMovement}.
     */
    public HashSet<CardMovement> getMovements() {
        return movements;
    }
}
