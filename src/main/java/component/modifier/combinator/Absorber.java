package component.modifier.combinator;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Absorber} is a stateful {@link Combinator} that transfers the 
 * identity of one card to another.
 * <p>
 * The process occurs in two phases:
 * <ol>
 * <li><b>Phase 1:</b> The first card to enter is "absorbed" (removed from the board) 
 * and stored internally.</li>
 * <li><b>Phase 2:</b> The next card to enter inherits the numerical value of 
 * the absorbed card.</li>
 * </ol>
 */
public class Absorber extends Combinator {

    /** The card currently stored in the buffer, waiting to transfer its value. */
    private Card previousCard;

    /** * Handles the two-step modification logic.
     * <p>
     * This method respects the standard immunities:
     * <ul>
     * <li>{@link #checkSetDisable}: Checks for Corrupted material.</li>
     * <li>{@link #checkDestroyGlass}: Checks if a Glass card should shatter.</li>
     * <li>{@link #checkStone}: Checks if a Stone card is immune to combinators.</li>
     * </ul>
     * @param toModify The card currently entering the Absorber's tile.
     */
    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;
        if (checkStone(toModify)) return;

        if (toModify == null) return;

        if (previousCard != null) {
            // Phase 2: Transfer the value of the stored card to the new card
            toModify.setValue(previousCard.getValue());
            previousCard = null; // Buffer cleared
        }
        else {
            // Phase 1: Store the current card and remove it from the level
            previousCard = toModify;
            GameLevel.getInstance().removeCard(toModify);
        }

        onSuccess();
    }

    /** * @return {@code false}, allowing cards to move into this tile freely.
     */
    @Override
    public boolean isBlocking() {
        return false;
    }

    /** * Resets the modifier, re-enabling it and clearing any stored card buffer.
     */
    @Override
    public void reset() {
        super.reset();
        previousCard = null;
    }

    /** * Generates a dynamic tooltip.
     * <p>
     * If a card is currently absorbed, the tooltip will explicitly state which 
     * card is being "held" in the buffer using {@link Tooltip#ref(Object)}.
     * @return A {@link Tooltip} detailing the absorber's state and function.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Absorber",
            Color.DEEPPINK,
            "A ",
            getCombinatorTooltip(), // Shared combinator description
            " that will destroy the first card that comes in and then ",
            "sets the destroyed card's value onto the next card. ",
            (previousCard != null) ? "It has absorbed the " : null,
            (previousCard != null) ? Tooltip.ref(previousCard) : null,
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}