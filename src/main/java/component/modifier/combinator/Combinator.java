package component.modifier.combinator;

import component.card.Card;
import component.card.Material;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

/**
 * The {@code Combinator} class is an abstract base for modifiers that alter 
 * the card count within a {@link logic.GameLevel}.
 * <p>
 * This class introduces the {@link Material#STONE} immunity logic. Because 
 * stone cards are heavy and immutable, they cannot be absorbed, merged, 
 * or split by combinator logic.
 */
public abstract class Combinator extends Modifier {

    /**
     * Resets the combinator to its default state, specifically re-enabling 
     * it after it has been disabled.
     */
    @Override
    public void reset() {
        this.setDisabled(false);
    }

    /**
     * Retrieves the tooltip for this specific combinator instance.
     * @return A {@link Tooltip} based on the combinator's functional category.
     */
    @Override
    public Tooltip getTooltip() {
        return getCombinatorTooltip();
    }

    /**
     * Static helper that provides a standardized description for all combinator types.
     * <p>
     * Useful for UI elements that need to explain the general concept of card 
     * combination and destruction.
     * @return A {@link Tooltip} explaining that this component changes the total 
     * amount of cards in play.
     */
    public static Tooltip getCombinatorTooltip() {
        return new Tooltip(
                "Combinator",
                Color.DARKORCHID,
                "A type of ",
                Tooltip.ref(Modifier.getModifierTooltip()),
                " that changes the total amount of cards in play."
        );
    }

    /**
     * Checks if a card is made of Stone and therefore immune to combinator effects.
     * <p>
     * Stone cards act as an "anti-mechanic" for combinators, forcing players 
     * to find ways to change the material before they can merge or absorb the card.
     * @param card The card to inspect.
     * @return {@code true} if the card is made of {@link Material#STONE}.
     */
    public static boolean checkStone(Card card) {
        if (card == null) return false;
        return card.getMaterial() == Material.STONE;
    }
}