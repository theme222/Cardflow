package component.card;

import component.modifier.Modifier;
import component.modifier.changer.Adder;
import component.modifier.changer.Changer;
import component.modifier.combinator.Combinator;
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
        return switch (this) {
            case PLASTIC -> new Tooltip(
            "Plastic",
                Color.BROWN
//                "The default material" // I don't think we should show this
            );
            case GLASS -> new Tooltip(
            "Glass",
                Color.BROWN,
                "This material is very fragile. It will break after passing through ",
                Tooltip.ref(3),
                " ",
                Tooltip.ref(Modifier.getModifierTooltip()),
                "s"
            );
            case METAL -> new Tooltip(
                    "Metal",
                    Color.BROWN,
                    "This material will be unaffected by ",
                    Tooltip.ref(Changer.getModifierTooltip()),
                    "s"
            );
            case STONE -> new Tooltip(
                    "Stone",
                    Color.BROWN,
                    "This material will be unaffected by ",
                    Tooltip.ref(Combinator.getCombinatorTooltip()),
                    "s"
            );
            case RUBBER -> new Tooltip(
                    "Rubber",
                    Color.BROWN,
                    "Extra bouncy! This material will apply effects from ",
                    Tooltip.ref(Modifier.getModifierTooltip()),
                    " twice"
            );
            case CORRUPTED -> new Tooltip(
                    "Corrupted",
                    Color.BROWN,
                    "This material will disable the first ",
                    Tooltip.ref(Modifier.getModifierTooltip()),
                    " it enters. It then reverts to a ",
                    Tooltip.ref(PLASTIC),
                    " card"
            );
        };
    }
}
