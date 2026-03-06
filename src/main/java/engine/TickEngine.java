package engine;

import engine.event.ModifyEndedEvent;
import engine.event.MovementEndedEvent;
import engine.event.PausedEvent;
import engine.event.ResetEvent;
import event.EventBus;
import event.RenderEvent;
import logic.GameLevel;
import javafx.animation.AnimationTimer;
import util.Config;

/**
 * The {@code TickEngine} is the central controller for the game's execution clock.
 * <p>
 * It utilizes a JavaFX {@link AnimationTimer} to trigger game logic at intervals 
 * defined in {@link Config#TICK_DURATION_MS}. The engine alternates between 
 * movement and modification phases to maintain a consistent simulation state.
 */
public class TickEngine {

    private static EngineState engineState = EngineState.PAUSED;
    private static GameState gameState = GameState.PLACING;

    private static long lastTick = 0;
    /** The tick interval converted to nanoseconds for high-precision timing. */
    private static final long TICK_INTERVAL_NS = (long) (Config.TICK_DURATION_MS * 1e6);

    /** * The core timer loop. It checks the elapsed time since the {@code lastTick} 
     * and triggers a logic update if the interval has passed.
     */
    private static final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (engineState != EngineState.RUNNING) return;

            if (now - lastTick >= TICK_INTERVAL_NS) {
                tick();
                lastTick = now;
            }
        }
    };

    /**
     * Transitions the game to the {@link GameState#SIMULATING} state and 
     * starts the execution clock.
     */
    public static void play() {
        gameState = GameState.SIMULATING;
        engineState = EngineState.RUNNING;
        startTimer();
    }

    /**
     * Suspends the execution clock and sets the engine to {@link EngineState#PAUSED}.
     */
    public static void pause() {
        engineState = EngineState.PAUSED;
        stopTimer();
    }

    /**
     * Executes a single {@link TickPhase} manually. 
     * Useful for debugging or "frame-by-frame" advancement while paused.
     */
    public static void step() {
        gameState = GameState.SIMULATING;
        if (engineState == EngineState.PAUSED) {
            tick();
        }
    }

    /**
     * Stops the simulation, resets the level data via {@link GameLevel#resetLevel()}, 
     * and restores the engine to the initial {@link GameState#PLACING} state.
     */
    public static void reset() {
        pause();
        currentPhase = TickPhase.MODIFY;
        gameState = GameState.PLACING;
        GameLevel.getInstance().resetLevel();
        EventBus.emit(new RenderEvent(GameLevel.getInstance().changedPoints));
        EventBus.emit(new ResetEvent());
    }

    /**
     * Internal method to initialize the timer start point and begin the loop.
     */
    private static void startTimer() {
        if(lastTick != 0) return; 
        lastTick = System.nanoTime();
        timer.start();
    }

    /**
     * Internal method to stop the timer and notify the system via {@link PausedEvent}.
     */
    private static void stopTimer() {
        lastTick = 0;
        timer.stop();
        EventBus.emit(new PausedEvent());
    }

    private static TickPhase currentPhase = TickPhase.MODIFY;

    /**
     * The primary logic driver. Depending on the {@code currentPhase}, it either:
     * <ul>
     * <li>Moves cards according to Mover directions.</li>
     * <li>Triggers Modifier logic on all cards.</li>
     * </ul>
     * Finally, it emits a {@link RenderEvent} to update the UI visuals.
     */
    public static void tick() {
        if (GameLevel.getInstance() == null) return;

        switch (currentPhase) {
            case MOVEMENT:
                GameLevel.getInstance().doMovementTick();
                EventBus.emit(new MovementEndedEvent());
                break;
            case MODIFY:
                GameLevel.getInstance().doModifyTick();
                EventBus.emit(new ModifyEndedEvent());
                break;
            default:
                break;
        }

        // Notify renderers about changed points to update the screen
        EventBus.emit(new RenderEvent(GameLevel.getInstance().changedPoints));

        // Advance to the next phase in the cycle
        currentPhase = currentPhase.next();
    }

    /** @return The current {@link EngineState} (RUNNING or PAUSED). */
    public static EngineState getEngineState() {
        return engineState;
    }

    /** @return The current {@link GameState} (PLACING or SIMULATING). */
    public static GameState getGameState() {
        return gameState;
    }
}