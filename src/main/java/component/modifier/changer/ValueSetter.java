package component.modifier.changer;

import component.card.Card;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

/**
 * The {@code ValueSetter} class is a specialized {@link Setter} that overwrites 
 * a {@link Card}'s numerical value.
 * <p>
 * Unlike {@link Arithmetic} modifiers, which transform values relatively, 
 * this setter forces a card to a specific rank regardless of its previous value.
 */
public class ValueSetter extends Setter<Integer> {

    /**
     * Constructs a ValueSetter with a target rank.
     * @param changeValue The numerical value [1-13] to be assigned to cards.
     */
    public ValueSetter(int changeValue) {
        this.changeType = ChangeType.NUMBER;
        setChange(changeValue);
    }

    /**
     * Forces the target card's value to the rank stored in this modifier.
     * @param toModify The {@link Card} whose value will be overwritten.
     */
    @Override
    public void change(Card toModify) {
        if (toModify != null) {
            toModify.setValue(changeValue);
        }
    }

    /**
     * Updates the target value for this setter, enforcing standard card rank bounds.
     * <p>
     * Uses {@code Math.clamp} to ensure the target value remains between 1 (Ace) 
     * and 13 (King).
     * @param change The desired target integer.
     */
    @Override
    public void setChange(Integer change) {
        this.changeValue = Math.clamp(change, 1, 13);
    }

    /**
     * Generates a descriptive tooltip for the Value Setter.
     * <p>
     * Includes a dynamic reference to the target rank and indicates if the 
     * modifier has been disabled by external logic (like a Corrupted card).
     * @return A {@link Tooltip} detailing the setter's behavior.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Value Setter",
            Color.DARKCYAN,
            "A ",
            super.getTooltip(), // Inherits general Changer description
            " that changes the value of the card to ",
            Tooltip.ref(getChange()),
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}