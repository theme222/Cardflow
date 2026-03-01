package component.mover;

import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
import util.Direction;

public class FlipFlop extends Mover {
    private boolean isActive; // Default current direction otherwise opposite

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public FlipFlop(Direction rotation) {
        super(rotation);
        isActive = true;
    }

    @Override
    public Direction getDirectionStateless() {
        return isActive ? getRotation(): getRotation().opposite();
    }

    @Override
    public Direction getDirection() {
        Direction toReturn = getDirectionStateless();
        if (GameLevel.getInstance().getTile(getGridPos()).getCard() != null) {
            isActive = !isActive;
        }
        return toReturn;
    }

    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation(), getRotation().opposite()};
    }

    @Override
    public void reset() {setActive(true);}

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "FlipFlop",
                Color.INDIANRED, // racist
                "A ",
                Tooltip.ref(Mover.getMoverTooltip()),
                " that switches direction every time a card enters. ",
                "It will currently move the card ",
                Tooltip.ref(getDirectionStateless())
        );
    }
}
