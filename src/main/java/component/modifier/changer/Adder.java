package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Adder} class is a specific type of {@link Arithmetic} modifier that 
 * increases a card's numerical value.
 * <p>
 * It enforces a non-negative change value; to decrease a card's value, the 
 * {@code Subtractor} class should be used instead.
 */
public class Adder extends Arithmetic {

    /**
     * Constructs an Adder with a specified positive increment.
     * @param changeValue The amount to add to the card's value. 
     * Values less than 0 will be clamped to 0.
     */
    public Adder(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    /**
     * Sets the amount by which the card's value will be increased.
     * <p>
     * <b>Note:</b> This method enforces a lower bound of 0 using {@code Math.max}.
     * @param changeValue The positive integer to be added.
     */
    @Override
    public void setChange(Integer changeValue) {
        this.changeValue = Math.max(changeValue, 0);
    }

    /**
     * Applies the addition logic to the specified card.
     * <p>
     * The card's internal {@code setValue} method handles the wrapping logic 
     * (modulo 13) if the result exceeds the maximum card value (King).
     * @param toModify The {@link Card} whose value will be incremented.
     */
    @Override
    public void change(Card toModify) {
        if (toModify != null) {
            toModify.setValue(toModify.getValue() + changeValue);
        }
    }

    /**
     * Generates a descriptive tooltip for the Adder modifier.
     * <p>
     * The tooltip dynamically includes the change value and indicates 
     * if the modifier has been disabled (e.g., by a {@code Corrupted} card).
     * @return A {@link Tooltip} detailing the Adder's properties.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Adder",
            Color.DARKTURQUOISE,
            "A ",
            super.getTooltip(), // Inherits the general Changer description
            " that adds ",
            Tooltip.ref(getChange()),
            " to the value of the card",
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}