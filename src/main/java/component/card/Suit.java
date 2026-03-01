package component.card;

import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.Util;

public enum Suit implements Tippable {
    SPADE,
    HEART,
    DIAMOND,
    CLUB;

    @Override
    public String toString() {
        return Util.capitalize(this.name());
    }

    @Override
    public Tooltip getTooltip() {
        Color color = switch (this) {
            case SPADE -> Color.LIGHTGREEN;
            case HEART -> Color.RED;
            case DIAMOND -> Color.ORANGE;
            case CLUB -> Color.DARKBLUE;
        };

        return new Tooltip(toString(), color);
    }

}
