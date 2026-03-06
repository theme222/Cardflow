package engine;

/**
 * The {@code TickPhase} enum represents the individual steps within a single 
 * game logic cycle.
 * <p>
 * The simulation strictly alternates between these phases to ensure that 
 * card data is modified only after movement is finalized, and vice versa.
 */
public enum TickPhase {
    /** * The phase where {@link component.mover.Mover} components calculate 
     * and execute the physical translation of cards across the grid.
     */
    MOVEMENT,

    /** * The phase where {@link component.modifier.Modifier} components apply 
     * logic, arithmetic, or state changes to the card occupying their tile.
     */
    MODIFY;

    /** * Cached array of enum constants to avoid repeated calls to {@code values()}.
     */
    private static final TickPhase[] VALUES = values();

    /** * Transitions to the next phase in the simulation cycle.
     * <p>
     * This creates a continuous loop: 
     * {@code MOVEMENT} &rarr; {@code MODIFY} &rarr; {@code MOVEMENT}...
     * @return The succeeding {@code TickPhase}.
     */
    public TickPhase next() {
        return switch (this) {
            case MOVEMENT -> MODIFY;
            case MODIFY -> MOVEMENT;
        };
    }
}