package component.modifier.pathway;

import component.card.Card;
import logic.GameLevel;

public class Exit extends Pathway {

    public Exit() {
    }

    @Override
    public void modify() {
        Card toRemove = GameLevel.getInstance().getTile(getGridPos()).getCard();
        if (GameLevel.getInstance().removeCard(toRemove)) {
            System.out.println("Got card " + toRemove + " expected " + getCurrentCard(GameLevel.getInstance().OUTPUT_CARDS));
            GameLevel.getInstance().exitedCardsList.add(toRemove);
            currentIndex++;
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
