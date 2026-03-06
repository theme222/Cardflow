package engine.event;

import engine.EngineEvent;
import engine.TickPhase;
import event.Event;

/**
 * The {@code PausedEvent} is a global signal indicating that the game 
 * execution has been suspended.
 * <p>
 * Unlike {@link EngineEvent} subclasses, this does not belong to a specific 
 * {@link TickPhase} because it interrupts the engine entirely. It is typically 
 * triggered by user input or the game losing focus.
 */
public class PausedEvent implements Event {

    /**
     * Constructs a new PausedEvent.
     * <p>
     * When emitted via the {@code EventBus}, listeners such as the 
     * User Interface should respond by displaying a pause menu and 
     * stopping all real-time visual updates.
     */
    public PausedEvent() {
        // No state required; the type itself conveys the message.
    }
}