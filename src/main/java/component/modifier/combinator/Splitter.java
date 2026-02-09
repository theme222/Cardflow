package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;
import logic.GameLevel;

public class Splitter extends Modifier {

    private Card cardToSpawn;

    @Override
    public void modify() {
        Card toModify = GameLevel.getInstance().getTile(getGridPos()).getCard();
        if (cardToSpawn == null) {
            if (toModify == null) return;
            toModify.setValue(toModify.getValue() / 2);
            cardToSpawn = new Card(toModify.getSuit(),
                    (int)Math.ceil((double)toModify.getValue() / 2),
                    toModify.getMaterial());
        }
        else {
            if (GameLevel.getInstance().addCard(cardToSpawn, getGridPos()))
                cardToSpawn = null; // Added success
        }
    }

    @Override
    public boolean isBlocking() {
        return cardToSpawn != null;
    }
}
