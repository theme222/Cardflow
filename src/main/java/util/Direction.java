package util;
// Quitintessential Utility for Directions

import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;

/**
 * The {@code Direction} enum represents the cardinal directions and 
 * the relative movement within the game grid.
 */
public enum Direction implements Tippable {
    /** Upward movement (negative Y). */
    UP, 
    /** Rightward movement (positive X). */
    RIGHT, 
    /** Downward movement (positive Y). */
    DOWN, 
    /** Leftward movement (negative X). */
    LEFT, 
    /** Stationary state. */
    STAY;

    private static final Direction[] CARDINALS = {
        UP, RIGHT, DOWN, LEFT
    };

    /** @return The next cardinal direction in clockwise rotation. */
    public Direction next() { // clockwise rotation
        if (this == STAY) return STAY;
        return CARDINALS[(ordinal() + 1) % CARDINALS.length];
    }

    /** @return The previous cardinal direction (counterclockwise rotation). */
    public Direction prev() { // counterclockwise rotation
        if (this == STAY) return STAY;
        return CARDINALS[(ordinal() + CARDINALS.length - 1) % CARDINALS.length];
    }

    /** @return The direction directly opposite to this one. */
    public Direction opposite() {
        if (this == STAY) return STAY;
        return CARDINALS[(ordinal() + 2) % CARDINALS.length];
    }

    /** 
     * @param other The direction to compare against.
     * @return {@code true} if the given direction is opposite to this one.
     */
    public boolean isOpposite(Direction other) {
        return this.opposite() == other;
    }

    /** 
     * @param other The direction to compare against.
     * @return {@code true} if this direction is 90 degrees left of the given direction.
     */
    public boolean isLeftOf(Direction other) {
        return this == other.prev();
    }

    /** 
     * @param other The direction to compare against.
     * @return {@code true} if this direction is 90 degrees right of the given direction.
     */
    public boolean isRightOf(Direction other) {
        return this == other.next();
    }

    /** 
     * @param other The direction to compare against.
     * @return {@code true} if this direction is perpendicular to the given direction.
     */
    public boolean isPerpendicularOf(Direction other){
        return isLeftOf(other) || isRightOf(other);
    }

    /** @return The X-axis delta for this direction. */
    public int dx() {
        return switch (this) {
            case LEFT  -> -1;
            case RIGHT ->  1;
            default    ->  0;
        };
    }

    /** @return The Y-axis delta for this direction. */
    public int dy() {
        return switch (this) {
            case UP    -> -1;
            case DOWN  ->  1;
            default    ->  0;
        };
    }

    @Override
    public String toString() {
        return Util.capitalize(name());
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            toString(),
            Color.CADETBLUE
        );
    }

}

