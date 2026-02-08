package component.modifier;

import component.GridIndexable;
import component.card.Card;
import logic.level.GameLevel;

import java.awt.*;

abstract public class Modifier implements GridIndexable {
    private boolean isDisabled;
    protected Point gridPos;

    public Modifier() {
        this(new Point());
    }

    public Modifier(Point gridPos) {
        this.isDisabled = false;
        this.gridPos = new Point();
        setGridPos(gridPos);
    }

    // GETTERS & SETTERS //
    public boolean isDisabled() { return isDisabled; }
    public void setDisabled(boolean disabled) { isDisabled = disabled; }

    @Override
    public Point getGridPos() { return gridPos; }
    @Override
    public void setGridPos(Point point) {
        this.gridPos.x = Math.clamp(point.x, 0, GameLevel.MAX_WIDTH);
        this.gridPos.y = Math.clamp(point.y, 0, GameLevel.MAX_HEIGHT);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    // GETTERS & SETTERS //

    // isBlocking needs to be set on inherit.
     public abstract void modify();
}
