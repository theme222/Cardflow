package component.mover;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
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

    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation(), getRotation().prev()};
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "ParityFilter",
                Color.INDIANRED, // racist
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
