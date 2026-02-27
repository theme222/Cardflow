package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;
import logic.GameLevel;

public class Merger extends Combinator {

    private Card previousCard;

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify));
        if (checkDestroyGlass(toModify)) return;

        if (toModify == null) return;
        if (previousCard != null) {
            // Add value of previous card to this card
            toModify.setValue(toModify.getValue() + previousCard.getValue());
            previousCard = null;
        }
        else {
            previousCard = toModify;
            GameLevel.getInstance().removeCard(toModify);
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public void reset() { previousCard = null; }
}
