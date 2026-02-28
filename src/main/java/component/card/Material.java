package component.card;

import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.Util;

public enum Material implements Tippable {
    PLASTIC,
    GLASS,
    METAL,
    STONE,
    RUBBER,
    CORRUPTED;

    @Override
    public String toString() {
        return Util.capitalize(this.name());
    }

    @Override
    public Tooltip getTooltip() {
        String description = switch (this) {
            case PLASTIC -> "The default material.";
            case GLASS -> "Will break if it passes through more than two modifiers.";
            case METAL -> "Can't be affected by any and all changers.";
            case STONE ->  "Can't be affected by any and all combinators.";
            case RUBBER -> "Modifiers apply twice.";
            case CORRUPTED -> "Will permanently disable a modifier once it passes through. Reverts to plastic afterwords.";
        };

        return new Tooltip(this.toString(), Color.BROWN, description);
    }
}
