package component.modifier.pathway.event;

import java.util.ArrayList;

import component.card.Card;
import util.GridPos;

public class CardExitEvent extends PathwayEvent {
    private ArrayList<Card> exitedCards;
    private int countCorrectExits;
    private int countTotalExits;

    public CardExitEvent(GridPos pos, Card card, int countCorrectExits, int countTotalExits) {
        super(pos, card);
        this.countCorrectExits = countCorrectExits;
        this.countTotalExits = countTotalExits;
    }

    public int getCountCorrectExits() {
        return countCorrectExits;
    }

    public int getCountTotalExits() {
        return countTotalExits;
    }

    
    
}
