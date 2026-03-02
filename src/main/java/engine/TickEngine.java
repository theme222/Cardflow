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

public class TickEngine {

    private static EngineState state = EngineState.PAUSED;

    private static long lastTick = 0;
    private static final long TICK_INTERVAL_NS = (long) (Config.TICK_DURATION_MS * 1e6);

    private static final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (state != EngineState.RUNNING) return;

            if (now - lastTick >= TICK_INTERVAL_NS) {
                tick();
                lastTick = now;
            }
        }
    };

    public static void play() {
        state = EngineState.RUNNING;
        startTimer();
        System.out.println("Game started");
    }

    public static void pause() {
        state = EngineState.PAUSED;
        stopTimer();
    }

    public static void step() {
        if (state == EngineState.PAUSED) {
            tick();
        }
    }

    public static void reset() {
        pause();
        GameLevel.getInstance().resetLevel();
        EventBus.emit(new RenderEvent(GameLevel.getInstance().changedPoints));
        EventBus.emit(new ResetEvent());
    }

    private static void startTimer() {
        if(lastTick != 0) return; // Prevent restarting the timer if it's already running
        lastTick = System.nanoTime();
        timer.start();
    }

    private static void stopTimer() {
        lastTick = 0;
        timer.stop();
        EventBus.emit(new PausedEvent());
    }

    private static TickPhase currentPhase = TickPhase.MOVEMENT;

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

        // Notify renderers about changed points
        EventBus.emit(new RenderEvent(GameLevel.getInstance().changedPoints));

        currentPhase = currentPhase.next();
    }
}
