package component.modifier.combinator;

import component.card.Card;
import component.card.Material;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class Vaporizer extends Combinator {
    public Vaporizer() {}

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkStone(toModify)) return;
        if (toModify != null) GameLevel.getInstance().removeCard(toModify);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "Vaporizer",
                Color.DEEPPINK,
                "A ",
                super.getTooltip(), // combinator
                " that will destroy the card that comes in",
                isDisabled() ? "This is currently ": null,
                isDisabled() ? getDisabledTooltip(): null
        );
    }
}
