package component.modifier.changer;

import component.card.Card;
import component.modifier.Modifier;

import java.awt.*;

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

    @Override
    public boolean isBlocking() {
        return false;
    }
}
