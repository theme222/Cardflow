package component.modifier.changer;

import component.card.Card;
import component.card.Suit;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class SuitSetter extends Setter<Suit> {

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setSuit(changeValue);
    }

    public SuitSetter(Suit changeValue) {
        this.changeType = ChangeType.SUIT;
        setChange(changeValue);
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "Suit Setter",
                Color.DARKGRAY,
                "A ",
                super.getTooltip(), // changer
                " that changes the suit to ",
                Tooltip.ref(getChange())
        );
    }
}
