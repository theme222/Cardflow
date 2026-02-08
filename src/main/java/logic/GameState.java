package logic;

public class GameState {
    private static GameState instance;

    // here add the state of like planning, paused, playing, etc.

    public static GameState getInstance() {
        return instance;
    }

    public static void setInstance(GameState instance) {
        GameState.instance = instance;
    }
}
