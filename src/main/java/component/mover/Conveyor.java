package component.mover;

import component.modifier.Modifier;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;
import util.*;

/**
 * The {@code Conveyor} is the basic implementation of a {@link Mover}.
 * <p>
 * It provides a constant, unidirectional force that moves cards from the current 
 * tile to the adjacent tile specified by its {@link Direction}.
 */
public class Conveyor extends Mover {

    /**
     * Constructs a {@code Conveyor} with a fixed movement direction.
     * @param rotation The {@link Direction} (UP, DOWN, LEFT, RIGHT) this 
     * conveyor faces.
     */
    public Conveyor(Direction rotation) {
        super(rotation);
    }

    /**
     * Returns a string representation of the conveyor, including its 
     * current rotation.
     * @return A string in the format {@code "Conveyor{DIRECTION}"}.
     */
    @Override
    public String toString() {
        return "Conveyor{" + rotation + '}';
    }

    /**
     * Retrieves the primary movement direction of this conveyor.
     * <p>
     * Since a standard conveyor is stateless, this always matches the 
     * component's rotation.
     * @return The {@link Direction} this conveyor is pointing.
     */
    @Override
    public Direction getDirectionStateless() { 
        return rotation; 
    }

    /**
     * Defines the possible exit paths for a card on this tile.
     * @return A single-element array containing the conveyor's 
     * {@link #getRotation()}.
     */
    @Override
    public Direction[] getValidOutputDirections() {
        return new Direction[]{getRotation()};
    }

    /**
     * Generates a tooltip detailing the conveyor's type and its 
     * movement direction.
     * @return A {@link Tooltip} with the conveyor's identity and destination info.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Conveyor",
            Color.INDIANRED,
            "A ",
            Tooltip.ref(Mover.getMoverTooltip()),
            " that will send the card ",
            Tooltip.ref(getDirectionStateless())
        );
    }
}