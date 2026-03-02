package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class Divider extends Arithmetic { // Generic type argument can't be primitive. This works basically the same way tho.

    @Override
    public void setChange(Integer changeValue) { // Can't be <= 0
        this.changeValue = Math.max(changeValue, 1);
    }

    public Divider(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setValue(toModify.getValue() / changeValue);
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Divider",
            Color.DARKTURQUOISE,
            "A ",
            super.getTooltip(), // changer
            " that performs integer division to the value of the card. It currently devides by ",
            isDisabled() ? "This is currently ": null,
            isDisabled() ? getDisabledTooltip(): null
        );
    }
}
