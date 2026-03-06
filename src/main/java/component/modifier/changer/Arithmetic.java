package component.modifier.changer;

import component.card.Card;
import component.card.Material;

/**
 * The {@code Arithmetic} class is the base for all modifiers that perform 
 * mathematical operations on a card's numerical value.
 * <p>
 * This class introduces material-based logic: any {@link Card} with the 
 * {@link Material#METAL} property is immune to arithmetic modifications 
 * and will pass through these tiles unchanged.
 */
public abstract class Arithmetic extends Changer<Integer> {

    /**
     * Attempts to apply a mathematical modification to the given card.
     * <p>
     * Before applying the modification, it checks the card's material. 
     * If the card is made of {@link Material#METAL}, the modification is aborted. 
     * Otherwise, it proceeds to the standard {@link Changer#modify(Card)} workflow, 
     * which handles health reduction for fragile cards and eventual value changes.
     * * @param card The {@link Card} to be checked and potentially modified.
     */
    @Override
    public void modify(Card card) {
        if (card == null) return;
        
        // Metal cards are rigid and cannot have their numerical values 
        // altered by standard arithmetic modifiers.
        if (card.getMaterial() == Material.METAL) return;

        super.modify(card);
    }
}