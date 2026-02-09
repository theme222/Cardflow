package placement.mover;

import component.mover.Conveyor;
import placement.TileConstructor;
import util.Direction;

public class ConveyorConstructor extends TileConstructor<Conveyor> {

//    private Direction direction = Direction.UP;

    @Override
    public void preview() {
        // optional: ghost rendering, highlight, etc.
    }

    @Override
    public Conveyor construct() {
        return new Conveyor(Direction.UP);
    }
}
