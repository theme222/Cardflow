package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

public class Multiplier extends Arithmetic { // Generic type argument can't be primitive. This works basically the same way tho.

    @Override
    public void setChange(Integer changeValue) { // Can't be <= 0
        this.changeValue = Math.max(changeValue, 1);
    }

    public Multiplier(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setValue(toModify.getValue() * changeValue);
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Multiplier",
            Color.DARKTURQUOISE,
            "A ",
            super.getTooltip(),
            " that multiplies the value of the card by ",
            Tooltip.ref(getChange()),
            isDisabled() ? "This is currently ": null,
            isDisabled() ? getDisabledTooltip(): null
        );
    }
}
