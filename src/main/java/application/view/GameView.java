package application.view;

import application.ViewManager;
import audio.AudioManager;
import engine.TickEngine;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import logic.GameLevel;
import logic.PlayerInventory;
import logic.event.end.GameWinEvent;
import registry.render.RenderLayer;
import registry.render.RendererRegistry;
import ui.button.BackButton;
import ui.game.GameRenderStack;
import ui.game.GameWinOverlay;
import ui.levelinfo.LevelInfoPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ui.tooltip.TooltipLayer;
import util.Direction;
import util.GridPos;
import event.EventBus;
import event.RenderEvent;

/**
 * The main view class for the gameplay screen. 
 * <p>
 * This class orchestrates the layout of the game grid, the level information sidebar, 
 * and various overlays (tooltips, win screens). It also listens for global game events 
 * to trigger visual updates or transition the game state.
 */
public class GameView extends View {

    /** * The current active instance of GameView. 
     * Note: This may be null or stale if the ViewManager has switched views.
     */
    private static GameView instance;

    private final LevelInfoPane levelInfoPane;
    private final GameWinOverlay gameWinOverlay;
    private final GameRenderStack gameGrid;
    private final TooltipLayer tooltipLayer;
    
    /** Unregistration handles for event bus listeners to prevent memory leaks. */
    private Runnable unregisterUpdatePoints;
    private Runnable unregisterShowWinOverlay;

    /** * Forces a visual refresh of a tile and its four immediate neighbors.
     * Often used when a tile placement affects the adjacency logic of surrounding movers.
     * * @param pos The center grid position to update.
     */
    public void updateTileAndAdjacent(GridPos pos) {
        if(pos == null) return;
        updateIfValid(pos);

        updateIfValid(pos.addDirection(Direction.RIGHT));
        updateIfValid(pos.addDirection(Direction.LEFT));
        updateIfValid(pos.addDirection(Direction.DOWN));
        updateIfValid(pos.addDirection(Direction.UP));
    }

    /** * Updates a single tile's visual state if it exists within the current grid bounds.
     * @param pos The position to refresh.
     */
    private void updateIfValid(GridPos pos) {
        if(gameGrid != null)
            gameGrid.updateIfValid(pos);
    }

    /**
     * Constructs a new GameView for the specified level.
     * Initializes the UI hierarchy including the grid, inventory pane, and back button.
     * * @param level The game level data to be rendered.
     */
    public GameView(GameLevel level) {
        super();
        setInstance(this);

        GameLevel.setInstance(level);
        PlayerInventory.setInstance(new PlayerInventory(level));

        tooltipLayer = new TooltipLayer();
        gameWinOverlay = new GameWinOverlay();
        levelInfoPane = new LevelInfoPane(tooltipLayer);
        gameGrid = new GameRenderStack(level, tooltipLayer);

        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setSpacing(80);
        mainLayout.getChildren().addAll(gameGrid, levelInfoPane);

        root.getChildren().addAll(mainLayout, new BackButton(), tooltipLayer);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Register event listeners
        unregisterUpdatePoints = EventBus.register(RenderEvent.class, this::updatePoints);
        unregisterShowWinOverlay = EventBus.register(GameWinEvent.class, this::showGameWinOverlay);
    }

    /**
     * Called when the view becomes active. Resets the engine and starts game music.
     */
    @Override
    public void startup() {
        TickEngine.reset();
        AudioManager.playMusic("music-game");
    }

    /**
     * Called when navigating away from this view. 
     * Unregisters event listeners and cleans up UI components to ensure resources are freed.
     */
    @Override
    public void cleanup() {
        TickEngine.reset();
        unregisterShowWinOverlay.run();
        unregisterUpdatePoints.run();
        getLevelInfoPane().cleanup();
        getTooltipLayer().cleanup();
    }

    /** * Handles batch visual updates based on render events.
     * @param ev The event containing a list of grid points that changed during a tick.
     */
    public void updatePoints(RenderEvent ev) {
        for (GridPos point : ev.getChangedPoints()) {
            GameView.getInstance().updateIfValid(point);
        }
        ev.getChangedPoints().clear();
    }

    /** * Pauses the game and triggers the "Win" animation overlay.
     * The overlay drops from the top of the screen with a custom bounce effect.
     * * @param event The win event containing completion data.
     */
    public void showGameWinOverlay(GameWinEvent event) {
        TickEngine.pause();
        TranslateTransition drop = new TranslateTransition(Duration.millis(900), gameWinOverlay);
        drop.setFromY(-ViewManager.getInstance().scene.getHeight());
        drop.setToY(0);

        /** Custom bounce physics for the win overlay drop. */
        Interpolator BOUNCE = new Interpolator() {
            @Override
            protected double curve(double t) {
                if (t < 0.3636) {
                    return 7.5625 * t * t;
                } else if (t < 0.7272) {
                    t -= 0.5454;
                    return 7.5625 * t * t + 0.75;
                } else if (t < 0.9090) {
                    t -= 0.8181;
                    return 7.5625 * t * t + 0.9375;
                } else {
                    t -= 0.9545;
                    return 7.5625 * t * t + 0.984375;
                }
            }
        };

        drop.setInterpolator(BOUNCE);
        drop.play();
        root.getChildren().remove(gameWinOverlay);
        root.getChildren().add(gameWinOverlay);
        AudioManager.playMusic("music-win");
    }

    /** @return The pane containing level statistics and inventory. */
    public LevelInfoPane getLevelInfoPane() {
        return levelInfoPane;
    }

    /** @return The layer responsible for rendering tooltips over components. */
    public TooltipLayer getTooltipLayer() { return tooltipLayer; }

    /** @return The current static instance of the game view. */
    public static GameView getInstance() {
        return instance;
    }

    /** @param view Sets the global static instance of this view. */
    public static void setInstance(GameView view) {
        instance = view;
    }
}