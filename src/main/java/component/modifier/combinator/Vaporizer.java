package component.modifier.combinator;

import component.card.Card;
import component.card.Material;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

/**
 * The {@code Vaporizer} is a specialized {@link Combinator} designed for 
 * card removal.
 * <p>
 * It acts as a 1-to-0 operator: any card entering the tile is permanently 
 * removed from the level. This is essential for cleaning up unwanted cards 
 * to prevent board overflow or to satisfy specific level objectives.
 */
public class Vaporizer extends Combinator {
    
    public Vaporizer() {}

    /**
     * Immediately removes the card from the game world.
     * <p>
     * Logic flow:
     * <ul>
     * <li><b>Corruption Check:</b> A Corrupted card may disable this tile.</li>
     * <li><b>Stone Immunity:</b> Stone cards are too dense to be vaporized and 
     * will simply pass through.</li>
     * <li><b>Removal:</b> All other cards are deleted via {@link GameLevel#removeCard}.</li>
     * </ul>
     * @param toModify The card to be destroyed.
     */
    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        
        // Stone cards are immune to deletion by vaporizers.
        if (checkStone(toModify)) return;
        
        if (toModify != null) {
            GameLevel.getInstance().removeCard(toModify);
            onSuccess();
        }
    }

    /**
     * @return {@code false}. This tile is never impassable.
     */
    @Override
    public boolean isBlocking() {
        return false;
    }

    /**
     * Generates a tooltip detailing the Vaporizer's destructive purpose.
     * @return A {@link Tooltip} explaining the deletion mechanic.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "Vaporizer",
                Color.DEEPPINK,
                "A ",
                super.getTooltip(), // References the generic Combinator tooltip
                " that will destroy the card that comes in",
                isDisabled() ? "This is currently " : null,
                isDisabled() ? getDisabledTooltip() : null
        );
    }
}