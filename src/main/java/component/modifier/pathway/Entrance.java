package component.modifier.pathway;

import component.card.Card;
import component.modifier.pathway.event.CardEnterEvent;
import event.EventBus;
import logic.GameLevel;

public class Entrance extends Pathway { // Entrance and exit lives on the same layer as a modifier

    // TODO: ADD RESETTER TO THIS STATE

    public Entrance() {
    }

    @Override
    public void modify(Card toModify) {
        // Here toModify is just the card that is currently in the same tile as the entrance
        // So we genuinely ignore that bum

        Card toAdd = getCurrentCard(GameLevel.getInstance().INPUT_CARDS);
        if (toAdd == null) return;

        if (GameLevel.getInstance().addCard(toAdd, getGridPos())) {
            EventBus.emit(new CardEnterEvent(getGridPos(), toAdd));
            currentIndex++;
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
