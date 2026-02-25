package component.modifier.pathway;

import component.card.Card;
import util.CardCount;
import component.modifier.Modifier;

import java.util.List;

public abstract class Pathway extends Modifier { // Pathways can't be disabled

    protected int currentIndex;

    public Pathway() {
    }

    public Card getCurrentCard(List<CardCount> cardCountList) {
        // O(n) search every time this runs :skull: I doubt we NEED to speed it up tho
        int indexLeft = currentIndex;
        for (CardCount cardCount : cardCountList) {
            if (cardCount.getCount() < 0 || cardCount.getCount() > indexLeft)
                return new Card(cardCount.getCard());
            indexLeft -= cardCount.getCount();
        }
        return null;
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
