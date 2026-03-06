package component.mover;

import component.card.Card;
import component.card.Suit;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
import util.Direction;

/**
 * The {@code RedBlackFilter} is a conditional {@link Mover} that sorts cards 
 * based on the color associated with their {@link Suit}.
 * <p>
 * Routing logic:
 * <ul>
 * <li><b>Red (Heart/Diamond):</b> Follows the primary {@link #getRotation()}.</li>
 * <li><b>Black (Spade/Club):</b> Diverts to {@link Direction#prev()} (90° CCW).</li>
 * </ul>
 */
public class RedBlackFilter extends Mover {

    /**
     * Constructs a {@code RedBlackFilter} with a primary output direction.
     * @param rotation The default direction for red cards.
     */
    public RedBlackFilter(Direction rotation) {
        super(rotation);
    }

    /**
     * Determines the output direction based on the suit color of the occupying card.
     * <p>
     * If no card is present, returns {@link Direction#STAY}.
     * Otherwise, checks if the suit is HEART or DIAMOND to determine the path.
     * @return The resulting {@link Direction} for the card.
     */
    @Override
    public Direction getDirectionStateless() {
        Card toMove = GameLevel.getInstance().getTile(getGridPos()).getCard();

        if (toMove == null) return Direction.STAY;
        
        // Red suits (Heart/Diamond) pass through; Black suits (Spade/Club) turn left.
        boolean isRed = (toMove.getSuit() == Suit.HEART || toMove.getSuit() == Suit.DIAMOND);
        return isRed ? getRotation() : getRotation().prev();
    }

    /**
     * Returns the movement direction. 
     * Since this filter is stateless, it relies on {@link #getDirectionStateless()}.
     * @return The movement {@link Direction}.
     */
    @Override
    public Direction getDirection() {
        return getDirectionStateless();
    }

    /**
     * Defines the valid output paths for this filter.
     * @return An array containing the forward rotation and the previous (CCW) rotation.
     */
    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation(), getRotation().prev()};
    }

    /**
     * Generates a detailed tooltip explaining the suit-color routing rules.
     * @return A {@link Tooltip} displaying the sorting logic for red vs. black cards.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "RedBlackFilter",
            Color.INDIANRED,
            "A ",
            Tooltip.ref(Mover.getMoverTooltip()),
            " that filters cards based on the color of the suit.",
            " It will move the card ",
            Tooltip.ref(getRotation()),
            " if it is a ",
            Tooltip.ref(Suit.HEART),
            " or ",
            Tooltip.ref(Suit.DIAMOND),
            " otherwise it will move the card ",
            Tooltip.ref(getRotation().prev()),
            " if it is a ",
            Tooltip.ref(Suit.SPADE),
            " or ",
            Tooltip.ref(Suit.CLUB)
        );
    }
}