package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class Duplicator extends Combinator {

    private Card cardToSpawn;

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;

        if (cardToSpawn == null) {
            if (toModify == null) return;
            cardToSpawn = new Card(toModify); // copy over everything
        }
        else {
            if (GameLevel.getInstance().addCard(cardToSpawn, getGridPos()))
                cardToSpawn = null; // Added success
        }
    }

    @Override
    public boolean isBlocking() {
        return cardToSpawn != null;
    }

    @Override
    public void reset() { cardToSpawn = null; }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "Duplicator",
                Color.DEEPPINK,
                "A ",
                super.getTooltip(), // combinator
                " that will copy the card on the same tile ",
                "and spawn a duplicate card. ",
                (cardToSpawn != null) ? "It is about to spawn a ": null,
                (cardToSpawn != null) ? Tooltip.ref(cardToSpawn): null
        );
    }
}
