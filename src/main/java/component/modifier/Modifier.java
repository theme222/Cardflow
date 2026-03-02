package component.modifier;

import component.card.Card;
import component.card.Material;
import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.GridIndexable;
import logic.GameLevel;
import util.GridPos;

abstract public class Modifier implements GridIndexable, Tippable {
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
        if (isDisabled()) return true;
        if (card.getMaterial() == Material.CORRUPTED) {
            setDisabled(true);
            card.setMaterial(Material.PLASTIC);
        }
        return isDisabled();
    }

    public boolean checkDestroyGlass(Card card) { // returns whether we destroyed the card
        if (card == null) return false;
        card.setHealth(card.getHealth() - 1);
        if (card.getHealth() == 0) { // Exactly equal not <= because -1 is inf
            GameLevel.getInstance().removeCard(card);
            return true;
        }
        return false;
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
    public abstract void reset();

    @Override
    public Tooltip getTooltip() {
        return getModifierTooltip();
    }

    public static Tooltip getModifierTooltip() { // abstract class so we can't really call getTooltip
        return new Tooltip("Modifier", Color.FORESTGREEN, "A special machine that can modify a card that occupies the same tile as it");
    }

    public Tooltip getDisabledTooltip() { // what a name btw
        return isDisabled() ?
            new Tooltip(
            "Disabled", // Extra spaces so you can put it at the end of the tt
            Color.RED,
            "This modifier will not work anymore."
            )
        : null;
    }
}
