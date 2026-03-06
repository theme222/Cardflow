package component.mover;

import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
import util.Direction;

/**
 * The {@code FlipFlop} class is a stateful {@link Mover} that toggles between 
 * two opposing directions.
 * <p>
 * Every time a card successfully interacts with this tile, the internal state 
 * flips, causing the next card to be sent in the 180-degree opposite direction.
 */
public class FlipFlop extends Mover {
    /** * Determines which direction is currently active. 
     * If true, uses {@link #getRotation()}; if false, uses the opposite. 
     */
    private boolean isActive; 

    /**
     * Constructs a FlipFlop mover.
     * @param rotation The primary {@link Direction} the mover starts with.
     */
    public FlipFlop(Direction rotation) {
        super(rotation);
        isActive = true;
    }

    /**
     * Checks the current toggle state.
     * @return {@code true} if the primary direction is active.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Manually sets the toggle state.
     * @param active The new state to set.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Determines the current movement direction without triggering a toggle.
     * @return The currently active {@link Direction}.
     */
    @Override
    public Direction getDirectionStateless() {
        return isActive ? getRotation() : getRotation().opposite();
    }

    /**
     * Retrieves the movement direction and toggles the state for the next card.
     * <p>
     * The flip only occurs if a card is actually present on the tile, preventing 
     * the state from oscillating during empty logic ticks.
     * @return The {@link Direction} allowed for the current card.
     */
    @Override
    public Direction getDirection() {
        Direction toReturn = getDirectionStateless();
        // Only flip if there is a card here to trigger the switch
        if (GameLevel.getInstance().getTile(getGridPos()).getCard() != null) {
            isActive = !isActive;
        }
        return toReturn;
    }

    /**
     * Lists all possible directions this mover can point to.
     * @return An array containing the primary rotation and its opposite.
     */
    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation(), getRotation().opposite()};
    }

    /**
     * Resets the mover to its primary starting direction.
     */
    @Override
    public void reset() {
        setActive(true);
    }

    /**
     * Generates a dynamic tooltip showing the current routing direction.
     * @return A {@link Tooltip} explaining the alternating behavior.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "FlipFlop",
                Color.INDIANRED,
                "A ",
                Tooltip.ref(Mover.getMoverTooltip()),
                " that switches direction every time a card enters. ",
                "It will currently move the card ",
                Tooltip.ref(getDirectionStateless())
        );
    }
}