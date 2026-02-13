package component.mover;

import util.Direction;

public class Delay extends Mover {

    private boolean isActive;

    public Delay(Direction rot) {
        super(rot);
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public Direction getDirectionStateless() {
        return isActive ? getRotation(): Direction.STAY;
    }

    @Override
    public Direction getDirection() {
        Direction toReturn = getDirectionStateless();
        isActive = !isActive;
        return toReturn;
    }

}
