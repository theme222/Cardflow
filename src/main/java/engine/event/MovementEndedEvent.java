package engine.event;

import engine.EngineEvent;
import engine.TickPhase;

/**
 * The {@code MovementEndedEvent} is emitted by the game engine when all 
 * card translations across the grid have concluded for the current tick.
 * <p>
 * This event ensures that logic-heavy operations (like modifications) 
 * only occur once the cards are physically settled on their new tiles.
 */
public class MovementEndedEvent extends EngineEvent {

    /**
     * Constructs a new movement completion event.
     * <p>
     * Associates this event specifically with the {@link TickPhase#MOVEMENT} 
     * stage of the engine lifecycle.
     */
    public MovementEndedEvent() {
        super(TickPhase.MOVEMENT);
    }
}