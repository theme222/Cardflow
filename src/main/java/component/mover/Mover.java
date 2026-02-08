package component.mover;

import component.GridIndexable;
import logic.level.GameLevel;

import java.awt.*;

abstract public class Mover implements GridIndexable {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        STAY
    }

    public static Point getTranslationFromDirection(Direction direction) {
        return switch (direction) {
            case UP-> new Point(0, -1);
            case DOWN-> new Point(0, 1);
            case LEFT-> new Point(-1, 0);
            case RIGHT-> new Point(1, 0);
            default -> new Point(0,0);
        };
    }


    public abstract Direction getDirection();
//    public abstract void setDirection(Direction direction);

    protected Point gridPos;

    public Mover() {
        this.gridPos = new Point();
        setGridPos(gridPos);
    }

    @Override
    public Point getGridPos() { return gridPos; }
    @Override
    public void setGridPos(Point point) {
        this.gridPos.x = Math.clamp(point.x, 0, GameLevel.MAX_WIDTH);
        this.gridPos.y = Math.clamp(point.y, 0, GameLevel.MAX_HEIGHT);
    }

    public boolean isBlocking() {return false;}
}
