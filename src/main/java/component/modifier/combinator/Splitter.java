package component.modifier.combinator;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class Splitter extends Combinator {

    private Card cardToSpawn;

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;
        if (checkStone(toModify)) return;

        if (cardToSpawn == null) {
            if (toModify == null) return;
            int initialValue = toModify.getValue();
            toModify.setValue(initialValue / 2);
            cardToSpawn = new Card(toModify.getSuit(),
                    (int)Math.ceil((double)initialValue / 2),
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
    public void reset() {
        super.reset();
        cardToSpawn = null;
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Splitter",
            Color.DEEPPINK,
            "A ",
            getCombinatorTooltip(),
            " that will halve the value of the card ",
            "and spawn the second half afterwords",
            (cardToSpawn != null) ? "It is about to spawn a ": null,
            (cardToSpawn != null) ? Tooltip.ref(cardToSpawn): null,
            isDisabled() ? "This is currently ": null,
            isDisabled() ? getDisabledTooltip(): null
        );
    }
}
