package component.modifier.changer;

import component.card.Card;
import logic.GameLevel;

public class SuitSetter extends Changer<Card.Suit> {

    public void modify() {
        Card toModify = GameLevel.getInstance().getTile(getGridPos()).getCard();
        if (toModify != null) toModify.setSuit(changeValue);
    }

    public SuitSetter(Card.Suit changeValue) {
        this.changeType = ChangeType.SUIT;
        setChange(changeValue);
    }
}
