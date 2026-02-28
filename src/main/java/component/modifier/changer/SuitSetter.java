package component.modifier.changer;

import component.card.Card;
import component.card.Suit;
import logic.GameLevel;

public class SuitSetter extends Setter<Suit> {

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setSuit(changeValue);
    }

    public SuitSetter(Suit changeValue) {
        this.changeType = ChangeType.SUIT;
        setChange(changeValue);
    }
}
