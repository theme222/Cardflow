package component.modifier.combinator;

import component.card.Card;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Splitter} is a stateful {@link Combinator} that divides a card's 
 * numerical value and generates a second card from the remainder.
 * <p>
 * The operation happens in two steps:
 * <ol>
 * <li><b>The Split:</b> The incoming card's value is halved (integer division). 
 * A new card is created to hold the other half (using {@code Math.ceil} for 
 * odd values).</li>
 * <li><b>The Spawn:</b> On the next interaction, the newly created second 
 * card is added to the board.</li>
 * </ol>
 */
public class Splitter extends Combinator {

    /** The second half of the split card waiting to be placed on the grid. */
    private Card cardToSpawn;

    /**
     * Executes the splitting logic.
     * <p>
     * If an odd-valued card enters (e.g., 7), the original card becomes the 
     * lower half (3) and the spawned card becomes the upper half (4).
     * Standard material immunities (Corrupted, Glass, and Stone) apply.
     * @param toModify The {@link Card} currently entering the tile.
     */
    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;
        if (checkStone(toModify)) return;

        if (cardToSpawn == null) {
            if (toModify == null) return;
            
            int initialValue = toModify.getValue();
            
            // Set the original card to the "floor" value
            toModify.setValue(initialValue / 2);
            
            // Create a new card with the same suit and material, but the "ceiling" value
            cardToSpawn = new Card(toModify.getSuit(),
                    (int)Math.ceil((double)initialValue / 2),
                    toModify.getMaterial());
            
            onSuccess();
        }
        else {
            // Attempt to place the second half on the board
            if (GameLevel.getInstance().addCard(cardToSpawn, getGridPos())) {
                cardToSpawn = null; // Successfully added
                onSuccess();
            }
        }
    }

    /**
     * @return {@code true} if a card is waiting in the buffer, preventing 
     * new cards from entering until the split is complete.
     */
    @Override
    public boolean isBlocking() {
        return cardToSpawn != null;
    }

    /**
     * Resets the modifier, clearing the spawn buffer and re-enabling 
     * it if it was disabled.
     */
    @Override
    public void reset() {
        super.reset();
        cardToSpawn = null;
    }

    /**
     * Generates a tooltip explaining the split and identifying the 
     * value of the card currently waiting to spawn.
     * @return A {@link Tooltip} detailing the splitter's status.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Splitter",
            Color.DEEPPINK,
            "A ",
            getCombinatorTooltip(),
            " that will halve the value of the card ",
            "and spawn the second half afterwards. ",
            (cardToSpawn != null) ? "It is about to spawn a " : null,
            (cardToSpawn != null) ? Tooltip.ref(cardToSpawn) : null,
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}