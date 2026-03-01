package component.modifier.pathway;

import component.card.Card;
import component.modifier.Modifier;
import component.modifier.pathway.event.CardEnterEvent;
import event.EventBus;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

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

    @Override
    public Tooltip getTooltip() {
        Card toAdd = getCurrentCard(GameLevel.getInstance().INPUT_CARDS);

        return new Tooltip(
            "Entrance",
            Color.DEEPPINK,
            "A ",
            Tooltip.ref(Modifier.getModifierTooltip()), // combinator
            " that will spawn cards based on the level.",
            (toAdd != null) ? " It will attempt to spawn a ": null,
            (toAdd != null) ? Tooltip.ref(toAdd): null,
            " This can't be disabled"
        );
    }

}
