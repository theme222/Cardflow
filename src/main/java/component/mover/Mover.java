package component.mover;

import util.GridIndexable;
import logic.GameLevel;
import util.GridPos;

abstract public class Mover implements GridIndexable {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        STAY
    }

    public static GridPos getTranslationFromDirection(Direction direction) {
        return switch (direction) {
            case UP-> new GridPos(0, -1);
            case DOWN-> new GridPos(0, 1);
            case LEFT-> new GridPos(-1, 0);
            case RIGHT-> new GridPos(1, 0);
            default -> new GridPos(0,0);
        };
    }


    public abstract Direction getDirection();
//    public abstract void setDirection(Direction direction);

    protected GridPos gridPos;
    protected Direction inputRotation; // Ensure never STAY

    public Mover(Direction inputRotation) {
        this.gridPos = new GridPos();
        setGridPos(gridPos);
        setInputRotation(inputRotation);
    }


    public Direction getInputRotation() {
        return inputRotation;
    }

    public void setInputRotation(Direction inputRotation) {
        if (inputRotation == Direction.STAY) inputRotation = Direction.UP;
        this.inputRotation = inputRotation;
    }

    @Override
    public GridPos getGridPos() { return gridPos; }
    @Override
    public void setGridPos(GridPos point) {
        this.gridPos.setX(Math.clamp(point.getX(), 0, GameLevel.MAX_WIDTH));
        this.gridPos.setY(Math.clamp(point.getY(), 0, GameLevel.MAX_HEIGHT));
    }

    public boolean isBlocking() {return false;}
}
