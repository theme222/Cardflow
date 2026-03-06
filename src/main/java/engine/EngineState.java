package engine;

/**
 * The {@code EngineState} enum represents the execution states 
 * of the {@link TickEngine}.
 */
public enum EngineState {
    /** The engine is actively processing logic and movement ticks. */
    RUNNING,
    /** The engine has been paused and is currently idle. */
    PAUSED
}