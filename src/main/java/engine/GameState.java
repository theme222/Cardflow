package engine;

/**
 * The {@code GameState} enum represents the main phases of a level.
 */
public enum GameState {
    /** The player is placing tiles and configuring the board. */
    PLACING,
    /** The player has started the engine, and cards are moving. */
    SIMULATING
}
