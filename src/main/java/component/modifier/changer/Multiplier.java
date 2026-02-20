package component.modifier.changer;

import component.card.Card;
import logic.GameLevel;

public class Multiplier extends Changer<Integer> { // Generic type argument can't be primitive. This works basically the same way tho.

    @Override
    public void setChange(Integer changeValue) { // Can't be <= 0
        this.changeValue = Math.max(changeValue, 1);
    }

    public Multiplier(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        this.changeValue = changeValue;
    }

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setValue(toModify.getValue() * changeValue);
    }
}
