package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

public class ValueSetter extends Setter<Integer> {

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setValue(changeValue);
    }

    public ValueSetter(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    @Override
    public void setChange(Integer change) {
        this.changeValue = Math.clamp(change, 1, 13);
    }

    @Override
    public Tooltip getTooltip() {
        Object[] arr = {"a", "b"};
        return new Tooltip(
            "Value Setter",
            Color.DARKCYAN,
            "A ",
            super.getTooltip(), // changer
            " that changes the value of the card to ",
            Tooltip.ref(getChange()),
            isDisabled() ? "This is currently ": null,
            isDisabled() ? getDisabledTooltip(): null
        );
    }
}
