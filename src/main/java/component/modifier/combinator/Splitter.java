package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class Splitter extends Combinator {

    private Card cardToSpawn;

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;

        if (cardToSpawn == null) {
            if (toModify == null) return;
            toModify.setValue(toModify.getValue() / 2);
            cardToSpawn = new Card(toModify.getSuit(),
                    (int)Math.ceil((double)toModify.getValue() / 2),
                    toModify.getMaterial());
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
                "Splitter",
                Color.DEEPPINK,
                "A ",
                super.getTooltip(), // combinator
                " that will halve the value of the card ",
                "and spawn the second half afterwords"
        );
    }
}
