package component.modifier.pathway;

import component.card.Card;
import component.modifier.Modifier;
import logic.level.GameLevel;

public class Exit extends Modifier {
    int currentIndex;

    public Exit() {
        currentIndex = 0;
    }

    @Override
    public void modify() {
        Card toRemove = GameLevel.getInstance().getTile(getGridPos()).getCard();
        if (GameLevel.getInstance().removeCard(toRemove)) {
            currentIndex++;
            System.out.println("Card: " + toRemove + " has reached the end!");
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
