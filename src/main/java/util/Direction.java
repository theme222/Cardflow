package util;
// Quitintessential Utility for Directions

public enum Direction {
    UP, RIGHT, DOWN, LEFT, STAY;

    private static final Direction[] CARDINALS = {
        UP, RIGHT, DOWN, LEFT
    };

    public Direction next() { // clockwise rotation
        if (this == STAY) return STAY;
        return CARDINALS[(ordinal() + 1) % CARDINALS.length];
    }

    public Direction prev() { // counterclockwise rotation
        if (this == STAY) return STAY;
        return CARDINALS[(ordinal() + CARDINALS.length - 1) % CARDINALS.length];
    }

    public Direction opposite() {
        if (this == STAY) return STAY;
        return CARDINALS[(ordinal() + 2) % CARDINALS.length];
    }

    public boolean isOpposite(Direction other) {
        return this.opposite() == other;
    }

    public boolean isLeftOf(Direction other) {
        return this == other.prev();
    }

    public boolean isRightOf(Direction other) {
        return this == other.next();
    }

    public int dx() {
        return switch (this) {
            case LEFT  -> -1;
            case RIGHT ->  1;
            default    ->  0;
        };
    }

    public int dy() {
        return switch (this) {
            case UP    -> -1;
            case DOWN  ->  1;
            default    ->  0;
        };
    }


}

