package component.mover;

import java.awt.*;

public class Conveyor extends Mover {

    private Direction direction;

    public Conveyor(Direction direction) {
        setDirection(direction);
    }

    @Override
    public String toString() {
        return "Conveyor{"+direction+'}';
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
