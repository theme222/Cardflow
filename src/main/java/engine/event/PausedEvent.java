package engine.event;

import engine.EngineEvent;
import engine.TickPhase;

public class PausedEvent extends EngineEvent {
    public PausedEvent() {
        super(TickPhase.PAUSE);
    }
}

