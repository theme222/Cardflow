package component.mover;

import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.GridIndexable;
import util.*;

/**
 * The {@code Mover} class is the abstract foundation for all tiles that 
 * control card movement across the grid.
 * <p>
 * It provides the mathematical translation from abstract {@link Direction} 
 * to grid coordinates and manages the rotation state of the tile.
 */
abstract public class Mover implements GridIndexable, Tippable {

    protected GridPos gridPos;
    protected Direction rotation;

    /**
     * Constructs a {@code Mover} at a default position with a specified rotation.
     * @param rot The initial {@link Direction} the mover faces.
     */
    public Mover(Direction rot) {
        this.gridPos = new GridPos();
        setGridPos(gridPos);
        setRotation(rot);
    }

    // --- Directional Logic ---

    /**
     * Converts a {@link Direction} enum into a coordinate translation vector.
     * @param direction The direction to translate.
     * @return A {@link GridPos} representing the delta (e.g., UP returns 0, -1).
     */
    public static GridPos getTranslationFromDirection(Direction direction) {
        return switch (direction) {
            case UP -> new GridPos(0, -1);
            case DOWN -> new GridPos(0, 1);
            case LEFT -> new GridPos(-1, 0);
            case RIGHT -> new GridPos(1, 0);
            default -> new GridPos(0, 0);
        };
    }

    /**
     * Retrieves the current direction without triggering any internal state changes.
     * Useful for UI previews and pathfinding simulations.
     * @return The {@link Direction} the mover currently points toward.
     */
    public abstract Direction getDirectionStateless();

    /**
     * Retrieves the direction for actual movement execution.
     * <p>
     * Subclasses with state (like FlipFlops) should override this to trigger 
     * their internal logic when a card is moved.
     * @return The {@link Direction} the card should move this tick.
     */
    public Direction getDirection() { 
        return getDirectionStateless();
    }

    /**
     * Returns an array of all possible directions this mover could potentially 
     * send a card, used for rendering exit markers.
     * @return An array of {@link Direction} constants.
     */
    public abstract Direction[] getValidOutputDirections();

    // --- State Management ---

    /**
     * Increments the rotation of the mover to the next clockwise direction.
     */
    public void rotate() {
        setRotation(this.rotation.next());
    }

    /**
     * @return The primary {@link Direction} this mover is oriented toward.
     */
    public Direction getRotation() {
        return rotation;
    }

    /**
     * Sets the mover's rotation. If {@link Direction#STAY} is passed, 
     * it defaults to {@link Direction#UP} to ensure cards always have a vector.
     * @param rot The new {@link Direction}.
     */
    public void setRotation(Direction rot) {
        if (rot == Direction.STAY) rot = Direction.UP;
        this.rotation = rot;
    }

    @Override
    public GridPos getGridPos() { return gridPos; }

    @Override
    public void setGridPos(GridPos point) {
        this.gridPos.setX(Math.clamp(point.getX(), 0, GameLevel.MAX_WIDTH));
        this.gridPos.setY(Math.clamp(point.getY(), 0, GameLevel.MAX_HEIGHT));
    }

    /**
     * @return Always {@code false} for movers, as they are designed to 
     * be passed through.
     */
    public boolean isBlocking() { return false; }

    /**
     * Resets the mover's internal state for level restarts.
     */
    public void reset() { }

    @Override
    public Tooltip getTooltip() {
        return getMoverTooltip();
    }

    /**
     * Provides a generic tooltip for the Mover category.
     * @return A {@link Tooltip} with the Mover color theme (OliveDrab).
     */
    public static Tooltip getMoverTooltip() {
        return new Tooltip("Mover",
                Color.OLIVEDRAB,
                "Facilitates the transportation of cards"
        );
    }
}