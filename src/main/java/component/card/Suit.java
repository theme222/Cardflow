package component.card;

import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.Util;

/**
 * Represents the suit of a {@link Card}.
 * <p>
 * The {@code Suit} enum follows the standard French deck categories: 
 * Spades, Hearts, Diamonds, and Clubs. It is used by game logic to 
 * determine valid moves, win conditions, and UI styling.
 */
public enum Suit implements Tippable {
    /** The Spade suit, represented by a {@link Color#LIGHTGREEN} theme in the UI. */
    SPADE,
    
    /** The Heart suit, represented by a {@link Color#RED} theme in the UI. */
    HEART,
    
    /** The Diamond suit, represented by a {@link Color#ORANGE} theme in the UI. */
    DIAMOND,
    
    /** The Club suit, represented by a {@link Color#DARKBLUE} theme in the UI. */
    CLUB;

    /**
     * Converts the enum name to a capitalized string format.
     * @return A capitalized string (e.g., "Heart", "Spade").
     */
    @Override
    public String toString() {
        return Util.capitalize(this.name());
    }

    /**
     * Generates a {@link Tooltip} for the suit, primarily used for UI categorization.
     * <p>
     * Each suit is mapped to a specific color to help the player distinguish 
     * between them at a glance:
     * <ul>
     * <li>Spades: Light Green</li>
     * <li>Hearts: Red</li>
     * <li>Diamonds: Orange</li>
     * <li>Clubs: Dark Blue</li>
     * </ul>
     * @return A {@link Tooltip} containing the suit's name and its associated color theme.
     */
    @Override
    public Tooltip getTooltip() {
        Color color = switch (this) {
            case SPADE -> Color.LIGHTGREEN;
            case HEART -> Color.RED;
            case DIAMOND -> Color.ORANGE;
            case CLUB -> Color.DARKBLUE;
        };

        return new Tooltip(toString(), color);
    }
}