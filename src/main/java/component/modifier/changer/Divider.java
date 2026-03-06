package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Divider} class is a specific type of {@link Arithmetic} modifier that 
 * reduces a card's numerical value using integer division.
 * <p>
 * This class inherently performs "floor division." For example, if a card with 
 * a value of 7 passes through a Divider with a change value of 2, the new 
 * card value will be 3.
 */
public class Divider extends Arithmetic {

    /**
     * Constructs a Divider with a specified divisor.
     * @param changeValue The value to divide the card's value by. 
     * Values less than 1 will be clamped to 1 to prevent division by zero.
     */
    public Divider(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    /**
     * Sets the divisor for the operation.
     * <p>
     * <b>Safety Guard:</b> This method enforces a minimum value of 1. 
     * This ensures the game engine never encounters an {@link ArithmeticException} 
     * during a card's traversal.
     * @param changeValue The integer divisor.
     */
    @Override
    public void setChange(Integer changeValue) {
        this.changeValue = Math.max(changeValue, 1);
    }

    /**
     * Applies integer division to the specified card's value.
     * <p>
     * The result is passed to the card's {@code setValue} method, which continues 
     * to enforce the standard [1, 13] range logic.
     * @param toModify The {@link Card} whose value will be divided.
     */
    @Override
    public void change(Card toModify) {
        if (toModify != null) {
            toModify.setValue(toModify.getValue() / changeValue);
        }
    }

    /**
     * Generates a descriptive tooltip for the Divider modifier.
     * <p>
     * Explains the integer division behavior and dynamically reflects 
     * the current "Disabled" state if the modifier has been neutralized.
     * @return A {@link Tooltip} detailing the Divider's properties.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Divider",
            Color.DARKTURQUOISE,
            "A ",
            super.getTooltip(), // Inherits from Changer
            " that performs integer division to the value of the card. It currently divides by ",
            Tooltip.ref(getChange()),
            isDisabled() ? ". This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}