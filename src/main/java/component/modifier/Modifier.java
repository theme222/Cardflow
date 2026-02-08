package component.modifier;

import util.GridIndexable;
import logic.GameLevel;
import util.GridPos;

import java.awt.*;

abstract public class Modifier implements GridIndexable {
    private boolean isDisabled;
    protected GridPos gridPos;

    public Modifier() {
        this(new GridPos());
    }

    public Modifier(GridPos gridPos) {
        this.isDisabled = false;
        this.gridPos = new GridPos();
        setGridPos(gridPos);
    }

    // GETTERS & SETTERS //
    public boolean isDisabled() { return isDisabled; }
    public void setDisabled(boolean disabled) { isDisabled = disabled; }

    @Override
    public GridPos getGridPos() { return gridPos; }
    @Override
    public void setGridPos(GridPos point) {
        this.gridPos.setX(Math.clamp(point.getX(), 0, GameLevel.MAX_WIDTH));
        this.gridPos.setY(Math.clamp(point.getY(), 0, GameLevel.MAX_HEIGHT));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    // GETTERS & SETTERS //

    // isBlocking needs to be set on inherit.
     public abstract void modify();
}
