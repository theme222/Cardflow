package component.modifier.pathway;

import component.card.Card;
import component.modifier.Modifier;
import logic.level.GameLevel;

public class Entrance extends Modifier { // Entrance and exit lives on the same layer as a modifier

    // TODO: ADD RESETTER TO THIS STATE
    int currentIndex;

    public Entrance() {
        currentIndex = 0;
    }

    @Override
    public void modify() {
        Card toAdd = GameLevel.getInstance().inputCards.get(currentIndex);
        if (GameLevel.getInstance().addCard(toAdd, getGridPos())) currentIndex++;
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
