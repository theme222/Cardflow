package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

/**
 * The {@code Subtractor} class is an {@link Arithmetic} modifier that decreases 
 * a card's numerical value.
 * <p>
 * Like the {@code Adder}, it enforces a non-negative internal {@code changeValue} 
 * to maintain semantic clarity; if you want to increase a value, use an {@code Adder}.
 */
public class Subtractor extends Arithmetic {

    /**
     * Constructs a Subtractor with a specified positive decrement.
     * @param changeValue The amount to subtract from the card's value. 
     * If a negative number is provided, it is clamped to 0.
     */
    public Subtractor(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    /**
     * Sets the amount to be subtracted.
     * <p>
     * <b>Constraint:</b> This method enforces a lower bound of 0.
     * @param changeValue The positive integer decrement.
     */
    @Override
    public void setChange(Integer changeValue) {
        this.changeValue = Math.max(changeValue, 0);
    }

    /**
     * Decreases the specified card's value.
     * <p>
     * The subtraction is passed to the card's {@code setValue} logic, which 
     * handles underflows by wrapping back to the high end of the [1, 13] range.
     * @param toModify The {@link Card} whose value will be decreased.
     */
    @Override
    public void change(Card toModify) {
        if (toModify != null) {
            toModify.setValue(toModify.getValue() - changeValue);
        }
    }

    /**
     * Generates a descriptive tooltip for the Subtractor.
     * <p>
     * Includes the current decrement value and indicates if the modifier 
     * is currently disabled (e.g., via Corruption or Metal immunity).
     * @return A {@link Tooltip} detailing the Subtractor's properties.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Subtractor",
            Color.DARKTURQUOISE,
            "A ",
            super.getTooltip(),
            " that subtracts the value of the card by ",
            Tooltip.ref(getChange()),
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}