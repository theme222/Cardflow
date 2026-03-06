package registry.render;

/**
 * Enumerates the different layers used in the game's rendering stack.
 * Layers are typically rendered in the order they are defined.
 */
public enum RenderLayer {
    /** The bottom-most layer, usually the grid tiles. */
    BASE,
    /** Layer for movers (conveyors, filters, etc.). */
    MOVER,
    /** Layer for static modifiers. */
    MODIFIER,
    /** Layer for cards currently on tiles. */
    CARD,
    /** Layer for card movement animations. */
    CARDANIM,
    /** General UI overlay layer. */
    OVERLAY,
    /** Layer for visual effects (particles, highlights). */
    EFFECTS,
    /** Layer for mouse-hover highlights or information. */
    MOUSE_OVERLAY,
    /** Layer dedicated to handling mouse events. */
    MOUSE_EVENTS
}
