package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

public abstract class Combinator extends Modifier {
    // Used for rendering

    @Override
    public void reset() {this.setDisabled(false);}

    @Override
    public Tooltip getTooltip() {
        return getCombinatorTooltip();
    }

    public static Tooltip getCombinatorTooltip() {
        return new Tooltip(
                "Combinator",
                Color.AQUAMARINE,
                "A type of modifier that changes the total amount of cards in play."
        );
    }
}
