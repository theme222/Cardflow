package logic.event;

import java.util.HashSet;

import logic.movement.CardMovement;

public class AfterMovementEvent extends LogicEvent {
    final private HashSet<CardMovement> movements;

    public AfterMovementEvent(HashSet<CardMovement> movements) {
        super();
        this.movements = movements;
    }
    
    /** 
     * @return HashSet<CardMovement>
     */
    public HashSet<CardMovement> getMovements() {
        return movements;
    }
}
