package engine.event;

import event.Event;

/**
 * The {@code ResetEvent} is a global command used to restore the game board 
 * and engine to their initial level-start conditions.
 * <p>
 * This event is typically dispatched when a player clicks a "Restart" button 
 * or when a level fails. It acts as the trigger for all {@code GridIndexable} 
 * objects to invoke their respective {@code reset()} methods.
 */
public class ResetEvent implements Event {

    /**
     * Constructs a new ResetEvent.
     * <p>
     * Dispatching this event via the {@code EventBus} signals the 
     * {@code GameLevel} to wipe the current card states and the 
     * {@code Engine} to reset its tick counter and phase state.
     */
    public ResetEvent() {
        // Simple signal class; no internal data required.
    }
}