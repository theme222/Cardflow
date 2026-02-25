package component.mover;

import component.card.Card;
import logic.GameLevel;
import util.Direction;

public class ParityFilter extends Mover {
    // Checks whether the card is odd / even
    // If even pass through the current rotation
    // Otherwise odd goes to the left based on rotation

    public ParityFilter(Direction rotation) {
        super(rotation);
    }

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


}
