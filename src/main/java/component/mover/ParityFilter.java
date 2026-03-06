package component.mover;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
import util.Direction;

/**
 * The {@code ParityFilter} is a specialized {@link Mover} that redirects cards 
 * based on their numerical parity.
 * <p>
 * Even-valued cards are passed through in the mover's primary direction, 
 * while odd-valued cards are redirected 90 degrees to the left.
 */
public class ParityFilter extends Mover {

    /**
     * Constructs a new {@code ParityFilter} with a specific primary rotation.
     * @param rotation The primary direction for even cards.
     */
    public ParityFilter(Direction rotation) {
        super(rotation);
    }

    /** 
     * Determines the output direction based on the current card on the tile.
     * @return The primary rotation if the card value is even, otherwise the previous direction.
     */
    @Override
    public Direction getDirectionStateless() {
        Card toMove = GameLevel.getInstance().getTile(getGridPos()).getCard();

        if (toMove == null) return Direction.STAY;
        else if (toMove.getValue() % 2 == 0) return getRotation();
        else return getRotation().prev();
    }

    @Override
    public Direction getDirection() {
        return getDirectionStateless();
    }

    /** @return An array containing both possible exit directions. */
    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation(), getRotation().prev()};
    }

    /** @return A specialized {@link Tooltip} for the parity filter. */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "ParityFilter",
                Color.INDIANRED,
                "A ",
                Tooltip.ref(Mover.getMoverTooltip()),
                " that filters cards based on the parity of the value.",
                " It will move the card ",
                Tooltip.ref(getRotation()),
                " if the value is even otherwise it will move the card ",
                Tooltip.ref(getRotation().prev())
        );
    }
}
