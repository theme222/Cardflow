package component.modifier.combinator;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Merger} is a stateful {@link Combinator} that adds the value of 
 * one card into another, destroying the first in the process.
 * <p>
 * The sequence operates as follows:
 * <ol>
 * <li><b>First Card:</b> The card is removed from the level and its state is 
 * cached internally in {@code previousCard}.</li>
 * <li><b>Second Card:</b> The value of the cached card is added to the 
 * current card's value. The cache is then cleared.</li>
 * </ol>
 */
public class Merger extends Combinator {

    /** The card whose value is being held in transition. */
    private Card previousCard;

    /**
     * Executes the merging logic.
     * <p>
     * If a card is already cached, its value is added to {@code toModify}. 
     * Otherwise, {@code toModify} is cached and removed from play.
     * Standard material immunities (Corrupted, Glass, and Stone) apply.
     * @param toModify The {@link Card} interacting with the tile.
     */
    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;
        if (checkStone(toModify)) return;

        if (toModify == null) return;
        
        if (previousCard != null) {
            // Phase 2: Add the value of the absorbed card to the current card.
            // Note: Card.setValue() will handle the modulo-13 wrapping logic.
            toModify.setValue(toModify.getValue() + previousCard.getValue());
            previousCard = null;
        }
        else {
            // Phase 1: Store the card and remove it from the board.
            previousCard = toModify;
            GameLevel.getInstance().removeCard(toModify);
        }

        onSuccess();
    }

    /**
     * @return Always {@code false}, as cards must be able to pass through 
     * this tile to trigger the two-part merge.
     */
    @Override
    public boolean isBlocking() {
        return false;
    }

    /**
     * Resets the modifier, clearing any stored card data and re-enabling 
     * it if it was disabled.
     */
    @Override
    public void reset() {
        super.reset();
        previousCard = null;
    }

    /**
     * Generates a tooltip that dynamically displays the value currently 
     * waiting to be merged.
     * @return A {@link Tooltip} detailing the merger state and instructions.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Merger",
            Color.DEEPPINK,
            "A ",
            getCombinatorTooltip(),
            " that will destroy the first card that comes in and then ",
            "add the destroyed card's value onto the next card. ",
            (previousCard != null) ? "It will add " : null,
            (previousCard != null) ? Tooltip.ref(previousCard.getValue()) : null,
            (previousCard != null) ? " to the next card" : null,
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}