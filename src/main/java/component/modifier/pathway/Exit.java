package component.modifier.pathway;

import component.card.Card;
import component.modifier.pathway.event.CardExitEvent;
import event.EventBus;
import logic.GameLevel;

public class Exit extends Pathway {

    private int countCorrectExits = 0;
    private int countTotalExits = 0;

    public Exit() {
    }

    @Override
    public void modify() {
        Card toRemove = GameLevel.getInstance().getTile(getGridPos()).getCard();
        if (GameLevel.getInstance().removeCard(toRemove)) {
            Card expectedCard = getCurrentCard(GameLevel.getInstance().OUTPUT_CARDS);
            System.out.println("Got card " + toRemove + " expected " + expectedCard);
            
            
            if(toRemove.equals(expectedCard, true)) {
                System.out.println("Correct card exited!");
                countCorrectExits++;
            } else {
                System.out.println("Incorrect card exited!");
            }
            countTotalExits++;

            GameLevel.getInstance().exitedCardsList.add(toRemove);
            EventBus.emit(new CardExitEvent(getGridPos(), toRemove, countCorrectExits, countTotalExits));
            currentIndex++;
            
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
