package component.mover;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
import util.Direction;

/**
 * The {@code Delay} is a stateful {@link Mover} that stalls card movement.
 * <p>
 * A card entering this tile will be forced to remain for one additional logic tick
 * before the mover returns its actual rotation direction. This is essential for
 * timing synchronization in complex card pathways.
 */
public class Delay extends Mover {

    /** * The card instance detected during the previous logic tick. 
     * Used to determine if a card has "waited" long enough.
     */
    private Card cardToMove; 

    /**
     * Constructs a {@code Delay} mover with a specified exit direction.
     * @param rot The {@link Direction} the card will eventually move toward.
     */
    public Delay(Direction rot) {
        super(rot);
    }

    /** * Determines the movement direction without updating the internal state.
     * <p>
     * If the card currently on the tile matches the card stored during the last 
     * {@link #getDirection()} call, the delay is considered complete.
     * @return The {@link Direction} of rotation if the delay is over; 
     * otherwise {@link Direction#STAY}.
     */
    @Override
    public Direction getDirectionStateless() {
        // Check if the card on this tile is the same one we saw last tick
        boolean isActive = (cardToMove != null) && 
                           (cardToMove == GameLevel.getInstance().getTile(getGridPos()).getCard());
        return isActive ? getRotation() : Direction.STAY;
    }

    /** * Updates the delay state and returns the current allowed direction.
     * <p>
     * This method is called by the game engine during the movement phase. It 
     * returns the result of the delay check and then caches the current card 
     * to prepare for the next tick.
     * @return The movement {@link Direction} for the current tick.
     */
    @Override
    public Direction getDirection() {
        Direction toReturn = getDirectionStateless();
        // Update the buffer with whatever card is currently on the tile
        cardToMove = GameLevel.getInstance().getTile(getGridPos()).getCard();
        return toReturn;
    }

    /** * Defines the valid exit for this mover.
     * @return A single-element array containing the {@link Direction} of rotation.
     */
    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation()};
    }

    /** * Generates a tooltip explaining the 2-tick transit time.
     * @return A {@link Tooltip} detailing the delay mechanics.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "Delay",
                Color.INDIANRED,
                "A ",
                Tooltip.ref(Mover.getMoverTooltip()),
                " that takes ",
                Tooltip.ref(2), // 1 tick to 'see' it, 1 tick to 'move' it
                " ticks to move the card ",
                Tooltip.ref(getRotation())
        );
    }
}