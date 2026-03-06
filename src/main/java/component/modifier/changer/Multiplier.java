package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

/**
 * The {@code Multiplier} class is a specific type of {@link Arithmetic} modifier that 
 * scales a card's numerical value.
 * <p>
 * Because card values are restricted to the range [1, 13], large multipliers will 
 * trigger the card's internal wrapping logic frequently, allowing for rapid 
 * cycling through suits and values.
 */
public class Multiplier extends Arithmetic {

    /**
     * Constructs a Multiplier with a specified scale factor.
     * @param changeValue The factor to multiply the card's value by. 
     * Values less than 1 are clamped to 1 to prevent value annihilation (multiplying by 0).
     */
    public Multiplier(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    /**
     * Sets the scaling factor for the operation.
     * <p>
     * <b>Safety Guard:</b> This method enforces a minimum value of 1. 
     * This prevents cards from being stuck at 0 (which would wrap to King and stay 
     * there if multiplied by 0 again).
     * @param changeValue The integer multiplier.
     */
    @Override
    public void setChange(Integer changeValue) {
        this.changeValue = Math.max(changeValue, 1);
    }

    /**
     * Multiplies the specified card's value by the stored factor.
     * <p>
     * The card's internal {@code setValue} handles the modulo-13 wrapping 
     * automatically to keep the value within valid playing card bounds.
     * @param toModify The {@link Card} to be scaled.
     */
    @Override
    public void change(Card toModify) {
        if (toModify != null) {
            toModify.setValue(toModify.getValue() * changeValue);
        }
    }

    /**
     * Generates a descriptive tooltip for the Multiplier.
     * <p>
     * Reflects the current multiplier factor and any disabled status effects.
     * @return A {@link Tooltip} detailing the multiplier's behavior.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Multiplier",
            Color.DARKTURQUOISE,
            "A ",
            super.getTooltip(),
            " that multiplies the value of the card by ",
            Tooltip.ref(getChange()),
            isDisabled() ? ". This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}