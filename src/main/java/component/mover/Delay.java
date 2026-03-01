package component.mover;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
import util.Direction;

public class Delay extends Mover {

    private Card cardToMove; // The current card it was trying to move if it isn't the same (because it got vaporized) then it will reset

    public Delay(Direction rot) {
        super(rot);
    }

    @Override
    public Direction getDirectionStateless() {
        boolean isActive = cardToMove == GameLevel.getInstance().getTile(getGridPos()).getCard();
        return isActive ? getRotation(): Direction.STAY;
    }

    @Override
    public Direction getDirection() {
        Direction toReturn = getDirectionStateless();
        cardToMove = GameLevel.getInstance().getTile(getGridPos()).getCard();
        return toReturn;
    }

    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation()};
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "Delay",
                Color.INDIANRED, // racist
                "A ",
                Tooltip.ref(Mover.getMoverTooltip()),
                " that takes ",
                Tooltip.ref(2),
                " ticks to move the card ",
                Tooltip.ref(getDirectionStateless())
        );
    }
}
