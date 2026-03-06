package component.modifier.pathway;

import component.card.Card;
import component.modifier.Modifier;
import component.modifier.pathway.event.CardEnterEvent;
import event.EventBus;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Entrance} class is a specialized {@link Pathway} that handles 
 * the spawning of cards at the start of a level's logic loop.
 * <p>
 * It acts as the primary source of cards, pulling from the predefined 
 * input queue and emitting {@link CardEnterEvent} signals to notify 
 * the rest of the system that a new card has entered the board.
 */
public class Entrance extends Pathway {

    public Entrance() {
    }

    /**
     * Attempts to spawn the next card from the level's input queue.
     * <p>
     * Logic sequence:
     * 1. Fetches the current card from the {@code GameLevel.INPUT_CARDS} list.
     * 2. If a card is available, it tries to place it on this tile's coordinates.
     * 3. Upon success, it emits a {@link CardEnterEvent} via the {@link EventBus}.
     * 4. Increments the {@code currentIndex} to prepare for the next spawn cycle.
     * * @param toModify The card currently occupying the tile (ignored by this specific logic).
     */
    @Override
    public void modify(Card toModify) {
        // We focus on spawning new cards rather than modifying existing ones on this tile.
        Card toAdd = getCurrentCard(GameLevel.getInstance().INPUT_CARDS);
        if (toAdd == null) return;

        if (GameLevel.getInstance().addCard(toAdd, getGridPos())) {
            // Signal the game that a new card is active on the board
            EventBus.emit(new CardEnterEvent(getGridPos(), toAdd));
            currentIndex++;
            onSuccess();
        }
    }

    /**
     * @return {@code false}. Entrances must never block movement, 
     * as cards need to be able to move away from the spawn point.
     */
    @Override
    public boolean isBlocking() {
        return false;
    }

    /**
     * Generates a tooltip that previews the next card to be spawned.
     * <p>
     * This provides crucial information to the player, allowing them to 
     * anticipate which card value or suit is coming next.
     * * @return A {@link Tooltip} detailing the entrance state.
     */
    @Override
    public Tooltip getTooltip() {
        Card toAdd = getCurrentCard(GameLevel.getInstance().INPUT_CARDS);

        return new Tooltip(
            "Entrance",
            Color.DEEPPINK,
            "A ",
            Tooltip.ref(Modifier.getModifierTooltip()),
            " that will spawn cards based on the level.",
            (toAdd != null) ? " It will attempt to spawn a " : null,
            (toAdd != null) ? Tooltip.ref(toAdd) : null,
            " This can't be disabled"
        );
    }
}