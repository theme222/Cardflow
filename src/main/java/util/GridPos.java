package util;

import java.util.Objects;

/**
 * The {@code GridPos} class represents a 2D coordinate on the game grid.
 * <p>
 * It provides basic vector-like operations such as addition, 
 * direction calculation, and adjacency checks.
 */
public class GridPos {

    private int x;
    private int y;

    /** Constructs a {@code GridPos} at (0, 0). */
    public GridPos() {
        this(0, 0);
    }

    /** 
     * Copy constructor for {@code GridPos}.
     * @param other The {@code GridPos} to copy.
     */
    public GridPos(GridPos other) {
        if (other == null)
            other = new GridPos();
        this.x = other.x;
        this.y = other.y;
    }

    /** 
     * Constructs a {@code GridPos} with specific coordinates.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     */
    public GridPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** @return The X coordinate. */
    public int getX() {
        return x;
    }

    /** @param x The new X coordinate. */
    public void setX(int x) {
        this.x = x;
    }

    /** @return The Y coordinate. */
    public int getY() {
        return y;
    }

    /** @param y The new Y coordinate. */
    public void setY(int y) {
        this.y = y;
    }

    /** 
     * Returns a new {@code GridPos} resulting from adding coordinates.
     * @param other The coordinate delta to add.
     * @return A new {@code GridPos} instance.
     */
    public GridPos add(GridPos other) {
        if (other == null)
            other = new GridPos();
        return new GridPos(
                this.x + other.x,
                this.y + other.y);
    }

    /** 
     * Calculates the cardinal direction from this position to another.
     * @param other The target position.
     * @return The {@link Direction} to the target, or {@code null} if not adjacent.
     */
    public Direction directionTo(GridPos other) {
        if (other == null)
            return null;

        int dx = other.x - this.x;
        int dy = other.y - this.y;

        for (Direction dir : Direction.values()) {
            if (dir.dx() == dx && dir.dy() == dy) {
                return dir;
            }
        }

        return null; // Not adjacent
    }

    /** 
     * Checks if a target position lies in a specific direction relative to this one.
     * @param other The position to check.
     * @param dir The direction to look in.
     * @return {@code true} if the target is in the specified direction.
     */
    public boolean isPosInDirection(GridPos other, Direction dir) {
        if (other == null || dir == null)
            return false;

        int dx = other.x - this.x;
        int dy = other.y - this.y;

        switch (dir) {
            case Direction.RIGHT:
                return dx > 0;
            case Direction.LEFT:
                return dx < 0;
            case Direction.DOWN:
                return dy > 0;
            case Direction.UP:
                return dy < 0;
            default:
                return false;
        }
    }

    /** 
     * Returns a new {@code GridPos} resulting from adding raw deltas.
     * @param x The X delta.
     * @param y The Y delta.
     * @return A new {@code GridPos} instance.
     */
    public GridPos add(int x, int y) {
        return new GridPos(this.x + x, this.y + y);
    }

    /** 
     * Returns a new {@code GridPos} offset by a specific direction.
     * @param direction The direction to move in.
     * @return A new {@code GridPos} instance.
     */
    public GridPos addDirection(Direction direction) {
        return new GridPos(this.x + direction.dx(), this.y + direction.dy());
    }

    /** 
     * Checks if this position falls within a rectangular range.
     * @param xMin Minimum X (inclusive).
     * @param xMax Maximum X (inclusive).
     * @param yMin Minimum Y (inclusive).
     * @param yMax Maximum Y (inclusive).
     * @return {@code true} if the position is within bounds.
     */
    public boolean inRange(int xMin, int xMax, int yMin, int yMax) {
        return x >= xMin && x <= xMax && y >= yMin && y <= yMax;
    }

    // --- equality & hashing ---

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof GridPos))
            return false;
        GridPos gridPos = (GridPos) o;
        return x == gridPos.x && y == gridPos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
