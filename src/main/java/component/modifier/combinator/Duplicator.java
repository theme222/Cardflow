package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;
import logic.GameLevel;

public class Duplicator extends Combinator {

    private Card cardToSpawn;

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;

        if (cardToSpawn == null) {
            if (toModify == null) return;
            cardToSpawn = new Card(toModify); // copy over everything
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
