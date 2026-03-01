package component.modifier.changer;

import component.card.Card;
import logic.GameLevel;

public class Subtractor extends Arithmetic { // Generic type argument can't be primitive. This works basically the same way tho.

    @Override
    public void setChange(Integer changeValue) { // Can't be negative (Please use Subtractor instead)
        this.changeValue = Math.max(changeValue, 0);
    }

    public Subtractor(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setValue(toModify.getValue() - changeValue);
    }
}
