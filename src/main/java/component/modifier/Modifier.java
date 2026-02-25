package component.modifier;

import component.card.Card;
import util.GridIndexable;
import logic.GameLevel;
import util.GridPos;

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

    public boolean checkSetDisable(Card card) {
        if (card == null) return isDisabled();
        if (card.getMaterial() == Card.Material.CORRUPTED) {
            setDisabled(true);
            card.setMaterial(Card.Material.PLASTIC);
        }
        return isDisabled();
    }

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
     public abstract void modify(Card toModify);
}
