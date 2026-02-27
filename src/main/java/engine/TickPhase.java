package engine;

public enum TickPhase {
    MOVEMENT,
    MODIFY;

    private static final TickPhase[] VALUES = values();

    public TickPhase next() {
        return switch (this) {
            case MOVEMENT -> MODIFY;
            case MODIFY -> MOVEMENT;
        };
    }
}