package component.modifier.changer;

import component.card.Card;

public class ValueSetter extends Setter<Integer> {

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setValue(changeValue);
    }

    public ValueSetter(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    @Override
    public void setChange(Integer change) {
        this.changeValue = Math.clamp(change, 1, 13);
    }
}
