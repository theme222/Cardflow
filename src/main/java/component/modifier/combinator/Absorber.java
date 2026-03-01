package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class Absorber extends Combinator {

    private Card previousCard;

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;

        if (toModify == null) return;
        if (previousCard != null) {
            // Add value of previous card to this card
            toModify.setValue(previousCard.getValue());
            previousCard = null;
        }
        else {
            previousCard = toModify;
            GameLevel.getInstance().removeCard(toModify);
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public void reset() { previousCard = null; }


    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Absorber",
            Color.DEEPPINK,
            "A ",
            super.getTooltip(), // combinator
            " that will destroy the first card that comes in and then ",
            "sets the destroyed card's value onto the next card. ",
            (previousCard != null) ? "It has absorbed the ": null,
            (previousCard != null) ? Tooltip.ref(previousCard): null
        );
    }
}
