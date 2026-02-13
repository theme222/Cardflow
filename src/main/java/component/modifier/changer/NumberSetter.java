package component.modifier.changer;

import component.card.Card;
import logic.GameLevel;

public class NumberSetter extends Changer<Integer> {

    public void modify() {
        Card toModify = GameLevel.getInstance().getTile(getGridPos()).getCard();
        if (toModify != null) toModify.setValue(changeValue);
    }

    public NumberSetter(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    @Override
    public void setChange(Integer change) {
        this.changeValue = Math.clamp(change, 1, 13);
    }
}
