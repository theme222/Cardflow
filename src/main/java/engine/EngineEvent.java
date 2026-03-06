package engine;

import event.Event;

/**
 * The {@code EngineEvent} class represents an event specifically tied to the 
 * internal execution cycle of the game engine.
 * <p>
 * It acts as a base class for phase-specific events (like movement or modification)
 * and ensures that any listener can identify which part of the tick sequence 
 * triggered the notification.
 */
public class EngineEvent implements Event {
    
    /** The specific phase of the game tick during which this event was generated. */
    private final TickPhase tickPhase;

    /**
     * Constructs a new EngineEvent associated with a specific simulation phase.
     * @param tickPhase The {@link TickPhase} (e.g., MOVEMENT, MODIFY) currently 
     * being processed by the engine.
     */
    public EngineEvent(TickPhase tickPhase) {
        this.tickPhase = tickPhase;
    }

    /** * Retrieves the tick phase associated with this event.
     * <p>
     * This allows event subscribers to filter logic based on the engine's 
     * current state or to synchronize animations with specific phase completions.
     * @return The {@link TickPhase} of this event.
     */
    public TickPhase getTickPhase() {
        return tickPhase;
    }
}