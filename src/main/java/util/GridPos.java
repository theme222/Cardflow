package util;

import java.util.Objects;

public class GridPos implements Cloneable {
    // Not using Point because awt is a different UI toolkit
    // Not using Point2D because it is a double pair that is immutable

    private int x;
    private int y;

    public GridPos() {
        this(0, 0);
    }

    public GridPos(GridPos other) {
        if (other == null)
            other = new GridPos();
        this.x = other.x;
        this.y = other.y;
    }

    public GridPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public GridPos add(GridPos other) {
        if (other == null)
            other = new GridPos();
        return new GridPos(
                this.x + other.x,
                this.y + other.y);
    }

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

    public GridPos add(int x, int y) {
        return new GridPos(this.x + x, this.y + y);
    }

    public GridPos addDirection(Direction direction) {
        return new GridPos(this.x + direction.dx(), this.y + direction.dy());
    }

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
