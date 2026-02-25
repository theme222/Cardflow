package component.mover;

import logic.GameLevel;
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

}
