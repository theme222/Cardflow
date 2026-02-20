package component.modifier.pathway;

import component.card.Card;
import logic.GameLevel;

public class Exit extends Pathway {

    public Exit() {
    }

    @Override
    public void modify(Card toModify) {
        if (GameLevel.getInstance().removeCard(toModify)) {
            System.out.println("Got card " + toModify + " expected " + getCurrentCard(GameLevel.getInstance().OUTPUT_CARDS));
            GameLevel.getInstance().exitedCardsList.add(toModify);
            currentIndex++;
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
