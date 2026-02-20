package component.modifier.changer;

import component.card.Card;
import component.modifier.Modifier;
import logic.GameLevel;

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
        if (toModify == null) return;
        if (toModify.getMaterial() == Card.Material.METAL) return;
        if (toModify.getMaterial() == Card.Material.RUBBER) change(toModify);
        change(toModify);
    }
}
