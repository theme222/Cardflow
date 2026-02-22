package component.modifier.changer;

import component.card.Card;
import logic.GameLevel;

public class SuitSetter extends Setter<Card.Suit> {

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setSuit(changeValue);
    }

    public SuitSetter(Card.Suit changeValue) {
        this.changeType = ChangeType.SUIT;
        setChange(changeValue);
    }
}
