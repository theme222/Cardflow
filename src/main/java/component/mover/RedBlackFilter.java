package component.mover;

import component.card.Card;
import component.card.Suit;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
import util.Direction;

public class RedBlackFilter extends Mover {

    // Checks whether the card is odd / even
    // If red (Heart, Diamond) pass through the current rotation
    // Otherwise black (Spade, Club) goes to the left based on rotation

    public RedBlackFilter(Direction rotation) {
        super(rotation);
    }

    @Override
    public Direction getDirectionStateless() {
        Card toMove = GameLevel.getInstance().getTile(getGridPos()).getCard();

        if (toMove == null) return Direction.STAY;
        else if (toMove.getSuit() == Suit.HEART || toMove.getSuit() == Suit.DIAMOND) return getRotation();
        else return getRotation().prev();
    }

    @Override
    public Direction getDirection() {
        return getDirectionStateless();
    }

    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation(), getRotation().prev()};
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "RedBlackFilter",
            Color.INDIANRED, // racist
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
