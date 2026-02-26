package engine;

public enum TickPhase {
    MOVEMENT,
    MODIFY,
    PAUSE;

    private static final TickPhase[] VALUES = values();

    public TickPhase next() {
        return switch (this) {
            case MOVEMENT -> MODIFY;
            case MODIFY -> MOVEMENT;
            default -> PAUSE;
        };
    }
}