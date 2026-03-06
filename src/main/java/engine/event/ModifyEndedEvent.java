package engine.event;

import engine.EngineEvent;
import engine.TickPhase;

/**
 * The {@code ModifyEndedEvent} is triggered by the engine once all 
 * active modifiers on the board have completed their {@code modify()} logic.
 * <p>
 * This event acts as a lifecycle hook, allowing other systems (like the UI 
 * or score controllers) to know that the card states are now stable 
 * for the current {@link TickPhase#MODIFY}.
 */
public class ModifyEndedEvent extends EngineEvent {

    /**
     * Constructs a new phase-completion event.
     * <p>
     * It identifies itself as belonging to the {@link TickPhase#MODIFY} 
     * stage of the engine's execution cycle.
     */
    public ModifyEndedEvent() {
        super(TickPhase.MODIFY);
    }
}