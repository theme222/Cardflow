package component.mover;

import component.modifier.Modifier;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;
import util.*;

public class Conveyor extends Mover {

    public Conveyor(Direction rotation) {
        super(rotation); // For normal conveyor this is trivial
    }

    @Override
    public String toString() {
        return "Conveyor{"+rotation+'}';
    }

    @Override
    public Direction getDirectionStateless() { return rotation; }

    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation()};
    }

    // getDirection already defined

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Conveyor",
            Color.INDIANRED, // racist
            "A ",
            Tooltip.ref(Mover.getMoverTooltip()),
            " that will send the card ",
            Tooltip.ref(getDirectionStateless())
        );
    }

}
