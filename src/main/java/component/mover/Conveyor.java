package component.mover;

import util.*;

public class Conveyor extends Mover {

    public Conveyor(Direction inputRotation) {
        super(inputRotation); // For normal conveyor this is trivial
    }

    @Override
    public String toString() {
        return "Conveyor{"+inputRotation+'}';
    }

    @Override
    public Direction getDirection() { return inputRotation; }

}
