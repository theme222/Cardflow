package component.modifier.changer;

import component.card.Card;
import component.card.Suit;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

/**
 * The {@code SuitSetter} class is a specialized {@link Setter} that changes 
 * a {@link Card}'s {@link Suit}.
 * <p>
 * Unlike arithmetic modifiers which are relative, this setter is absolute; 
 * it forces any card passing through it to adopt the specified suit, 
 * regardless of its previous state.
 */
public class SuitSetter extends Setter<Suit> {

    /**
     * Constructs a SuitSetter with the specified target suit.
     * @param changeValue The {@link Suit} that cards will be converted to.
     */
    public SuitSetter(Suit changeValue) {
        this.changeType = ChangeType.SUIT;
        setChange(changeValue);
    }

    /**
     * Changes the suit of the specified card to the target suit.
     * <p>
     * This operation is applied directly to the card entity. Note that 
     * this does not affect the card's value or material.
     * @param toModify The {@link Card} to be transformed.
     */
    @Override
    public void change(Card toModify) {
        if (toModify != null) {
            toModify.setSuit(changeValue);
        }
    }

    /**
     * Generates a descriptive tooltip for the Suit Setter.
     * <p>
     * Includes a dynamic reference to the target suit and displays its 
     * associated color theme. It also indicates if the modifier has been 
     * disabled by game effects.
     * @return A {@link Tooltip} detailing the setter's function and target suit.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Suit Setter",
            Color.DARKCYAN,
            "A ",
            super.getTooltip(), // Inherits general Changer description
            " that changes the suit to ",
            Tooltip.ref(getChange()),
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}