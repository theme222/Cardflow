package component.modifier.pathway;

import component.card.Card;
import component.modifier.Modifier;
import component.modifier.pathway.event.CardExitEvent;
import event.EventBus;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class Exit extends Pathway {

    private int countCorrectExits = 0;
    private int countTotalExits = 0;

    public Exit() {
    }

    @Override
    public void modify(Card toRemove) {
        if (GameLevel.getInstance().removeCard(toRemove)) {
            Card expectedCard = getCurrentCard(GameLevel.getInstance().OUTPUT_CARDS);
            System.out.println("Got card " + toRemove + " expected " + expectedCard);
            
            
            if(toRemove.isEquivalent(expectedCard)) {
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

    @Override
    public void reset() {
        super.reset();
        countCorrectExits = 0;
        countTotalExits = 0;
    }

    @Override
    public Tooltip getTooltip() {
        Card expectedCard = getCurrentCard(GameLevel.getInstance().OUTPUT_CARDS);

        return new Tooltip(
                "Entrance",
                Color.DEEPPINK,
                "A ",
                Tooltip.ref(Modifier.getModifierTooltip()),
                " that will send cards for evaluation. It will only care about suit and value. ",
                (expectedCard != null) ? " Currently expecting a ": null,
                (expectedCard != null) ? Tooltip.ref(expectedCard): null,
                (expectedCard != null) ? ".": null,
                " This can't be disabled."
        );
    }
}
