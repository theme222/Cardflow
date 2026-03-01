package component.modifier.changer;

import component.card.Card;
import component.card.Material;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

abstract public class Changer<T> extends Modifier { // I'm not sorry haha

    protected T changeValue;

    public Changer() {
    }

    public T getChange() {
        return changeValue;
    }

    public void setChange(T changeValue) {
        this.changeValue = changeValue;
    }

    public enum ChangeType {
        NUMBER,
        SUIT,
        MATERIAL
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    protected ChangeType changeType;

    public abstract void change(Card toModify); //

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;
        if (toModify == null) return;
        if (toModify.getMaterial() == Material.RUBBER) change(toModify);
        change(toModify);
    }

    @Override
    public void reset() {this.setDisabled(false);}


    @Override
    public Tooltip getTooltip() {
        return getChangerTooltip();
    }

    public static Tooltip getChangerTooltip() { // since its an abstract class we can't really instantiate it
        return new Tooltip(
                "Changer",
                Color.BLUEVIOLET,
                "A modifier that changes a property of a card."
        );
    }
}
