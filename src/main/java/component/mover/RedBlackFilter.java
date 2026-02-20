package component.mover;

import component.card.Card;
import logic.GameLevel;
import util.Direction;

public class RedBlackFilter extends Mover {

    // Checks whether the card is odd / even
    // If red (Heart, Diamond) pass through the current rotation
    // Otherwise black (Spade, Club) goes to the right based on rotation

    public RedBlackFilter(Direction rotation) {
        super(rotation);
    }

    @Override
    public Direction getDirectionStateless() {
        Card toMove = GameLevel.getInstance().getTile(getGridPos()).getCard();

        if (toMove == null) return Direction.STAY;
        else if (toMove.getSuit() == Card.Suit.HEART || toMove.getSuit() == Card.Suit.DIAMOND) return getRotation();
        else return getRotation().next();
    }

    @Override
    public Direction getDirection() {
        return getDirectionStateless();
    }


}
