package component.modifier.combinator;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Duplicator} class is a stateful {@link Combinator} that creates 
 * an exact copy of a {@link Card}.
 * <p>
 * The duplication process operates in two steps:
 * <ol>
 * <li><b>Scanning:</b> When a card enters, the modifier creates a deep copy 
 * of the card (preserving suit, value, and material) and stores it in a buffer.</li>
 * <li><b>Spawning:</b> On a subsequent logic tick or interaction, the modifier 
 * attempts to place the cloned card onto the board at its current location.</li>
 * </ol>
 */
public class Duplicator extends Combinator {

    /** The cloned card instance currently waiting to be added to the level. */
    private Card cardToSpawn;

    /**
     * Executes the duplication logic.
     * <p>
     * If the buffer is empty, it scans the incoming card. If the buffer is full, 
     * it attempts to deploy the clone via {@link GameLevel#addCard}.
     * Standard immunities (Corrupted, Glass, and Stone) are checked before processing.
     * @param toModify The card triggering the modification tile.
     */
    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;
        if (checkStone(toModify)) return;

        if (cardToSpawn == null) {
            if (toModify == null) return;
            // Capture state: uses the Card copy constructor
            cardToSpawn = new Card(toModify); 
            onSuccess();
        }
        else {
            // Deployment phase: try to place the card back into the world
            if (GameLevel.getInstance().addCard(cardToSpawn, getGridPos())) {
                cardToSpawn = null; // Buffer cleared upon successful placement
                onSuccess();
            }
        }
    }

    /**
     * Determines if this tile is impassable.
     * @return {@code true} if a clone is currently stored in the buffer, 
     * preventing other cards from entering until the clone is spawned.
     */
    @Override
    public boolean isBlocking() {
        return cardToSpawn != null;
    }

    /**
     * Resets the modifier, re-enabling it and deleting any unspawned clones.
     */
    @Override
    public void reset() {
        super.reset();
        cardToSpawn = null;
    }

    /**
     * Generates a tooltip that dynamically reflects the Duplicator's state.
     * <p>
     * If a clone is ready to be spawned, the tooltip identifies the card 
     * currently held in the "printing" buffer.
     * @return A {@link Tooltip} with the status and identity of the stored clone.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Duplicator",
            Color.DEEPPINK,
            "A ",
            getCombinatorTooltip(),
            " that will copy the card on the same tile ",
            "and spawn a duplicate card. ",
            (cardToSpawn != null) ? "It is about to spawn a " : null,
            (cardToSpawn != null) ? Tooltip.ref(cardToSpawn) : null,
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}