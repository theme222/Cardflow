package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class Adder extends Arithmetic { // Generic type argument can't be primitive. This works basically the same way tho.

    @Override
    public void setChange(Integer changeValue) { // Can't be negative (Please use Subtractor instead)
        this.changeValue = Math.max(changeValue, 0);
    }

    public Adder(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setValue(toModify.getValue() + changeValue);
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Adder",
            Color.DARKTURQUOISE,
            "A ",
            super.getTooltip(), // changer
            " that adds ",
            Tooltip.ref(getChange()),
            " to the value of the card",
            isDisabled() ? "This is currently ": null,
            isDisabled() ? getDisabledTooltip(): null
        );
    }
}
