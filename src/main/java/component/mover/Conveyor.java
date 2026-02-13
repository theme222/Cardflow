package component.mover;

import util.*;

public class Conveyor extends Mover {

    public Conveyor(Direction rotation) {
        super(rotation); // For normal conveyor this is trivial
    }

    @Override
    public String toString() {
        return "Conveyor{"+rotation+'}';
    }

    @Override
    public Direction getDirectionStateless() { return rotation; }

    // getDirection already defined

}
