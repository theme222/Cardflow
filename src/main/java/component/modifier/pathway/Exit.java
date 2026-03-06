package component.modifier.pathway;

import component.card.Card;
import component.modifier.Modifier;
import component.modifier.pathway.event.CardExitEvent;
import event.EventBus;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Exit} class is a specialized {@link Pathway} that evaluates cards 
 * to determine if level objectives have been met.
 * <p>
 * When a card enters this tile, it is removed from the board and compared 
 * against the current expected card in the level's output sequence.
 */
public class Exit extends Pathway {

    private int countCorrectExits = 0;
    private int countTotalExits = 0;

    public Exit() {
    }

    /**
     * Evaluates the card against the level's goal sequence.
     * <p>
     * Logic flow:
     * 1. Removes the card from the grid.
     * 2. Retrieves the {@code expectedCard} for the current index.
     * 3. Uses {@code isEquivalent} to check if the card matches the goal (ignoring material).
     * 4. Updates statistics and emits a {@link CardExitEvent} for the UI and GameLevel.
     * @param toRemove The card reaching the end of its path.
     */
    @Override
    public void modify(Card toRemove) {
        if (GameLevel.getInstance().removeCard(toRemove)) {
            Card expectedCard = getCurrentCard(GameLevel.getInstance().OUTPUT_CARDS);
            
            // Check if the player successfully modified the card to match the goal
            if(toRemove.isEquivalent(expectedCard)) {
                countCorrectExits++;
            } 
            
            countTotalExits++;

            // Log the exit for post-game statistics
            GameLevel.getInstance().exitedCardsList.add(toRemove);
            
            // Broadcast the result
            EventBus.emit(new CardExitEvent(getGridPos(), toRemove, countCorrectExits, countTotalExits));
            
            // Move to the next goal in the sequence
            currentIndex++;
            onSuccess();
        }
    }

    /**
     * @return {@code false}. Cards must be able to enter the exit to be evaluated.
     */
    @Override
    public boolean isBlocking() {
        return false;
    }

    /**
     * Resets the exit's internal counters for level restarts.
     */
    @Override
    public void reset() {
        super.reset();
        countCorrectExits = 0;
        countTotalExits = 0;
    }

    /**
     * Generates a tooltip showing what card the player needs to provide next.
     * @return A {@link Tooltip} with goal information.
     */
    @Override
    public Tooltip getTooltip() {
        Card expectedCard = getCurrentCard(GameLevel.getInstance().OUTPUT_CARDS);

        return new Tooltip(
                "Exit", // Fixed name from snippet
                Color.DEEPPINK,
                "A ",
                Tooltip.ref(Modifier.getModifierTooltip()),
                " that will send cards for evaluation. It will only care about suit and value. ",
                (expectedCard != null) ? " Currently expecting a " : null,
                (expectedCard != null) ? Tooltip.ref(expectedCard) : null,
                (expectedCard != null) ? "." : null,
                " This can't be disabled."
        );
    }
}