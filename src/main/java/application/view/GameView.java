package application.view;

import application.ViewManager;
import engine.TickEngine;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import logic.GameLevel;
import logic.PlayerInventory;
import logic.event.end.GameWinEvent;
import registry.render.RendererRegistry;
import ui.button.BackButton;
import ui.game.GameRenderStack;
import ui.game.GameWinOverlay;
import ui.levelinfo.LevelInfoPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import util.Direction;
import util.GridPos;
import event.EventBus;
import event.RenderEvent;

public class GameView extends View {

    private static GameView instance; // This value can be null and can be invalid (just because instance exists doesn't mean that it is the current view. You must check it on the viewManager.)
    private final LevelInfoPane levelInfoPane;
    private final GameWinOverlay gameWinOverlay;
    private final GameRenderStack gameGrid;
    private Runnable unregisterUpdatePoints;
    private Runnable unregisterShowWinOverlay;

    public void updateTileAndAdjacent(GridPos pos) {
        updateIfValid(pos);

        updateIfValid(pos.addDirection(Direction.RIGHT)); // right
        updateIfValid(pos.addDirection(Direction.LEFT)); // left
        updateIfValid(pos.addDirection(Direction.DOWN)); // down
        updateIfValid(pos.addDirection(Direction.UP)); // up
    }

    private void updateIfValid(GridPos pos) {
        gameGrid.updateIfValid(pos);
    }

    public GameView(GameLevel level) {
        super();
        setInstance(this);
        
        GameLevel.setInstance(level); // Most components will rely on this
        PlayerInventory.setInstance(new PlayerInventory(level));

        gameGrid = new GameRenderStack(level);

        VBox infoPane = new VBox();
        infoPane.setPadding(new Insets(10));

        levelInfoPane = new LevelInfoPane();

        gameWinOverlay = new GameWinOverlay();

        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setSpacing(80);
        mainLayout.getChildren().addAll(gameGrid, levelInfoPane);


        root.getChildren().addAll(mainLayout, new BackButton());
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));


        // Register to receive render events and update affected tiles
        unregisterUpdatePoints = EventBus.register(RenderEvent.class, this::updatePoints);
        unregisterShowWinOverlay = EventBus.register(GameWinEvent.class, this::showGameWinOverlay);
        TickEngine.reset();
    }

    @Override
    public void cleanup() {
        TickEngine.reset();
        unregisterShowWinOverlay.run();
        unregisterUpdatePoints.run();
        getLevelInfoPane().cleanup();
    }

    public void updatePoints(RenderEvent ev) {
        for (GridPos point : ev.getChangedPoints()) {
            GameView.getInstance().updateIfValid(point); // I still don't know whats going on actually
        }
        ev.getChangedPoints().clear(); // Clearing here instead of inside gameLevel
    }

    public void showGameWinOverlay(GameWinEvent event) {
        TickEngine.pause();
        TranslateTransition drop = new TranslateTransition(Duration.millis(900), gameWinOverlay);
        drop.setFromY(-ViewManager.getInstance().scene.getHeight());
        drop.setToY(0);

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
        }; // Bounce effect (thx chatgpt)

        drop.setInterpolator(BOUNCE);
        drop.play();
        root.getChildren().remove(gameWinOverlay);
        root.getChildren().add(gameWinOverlay); // honestly idk
    }

    public LevelInfoPane getLevelInfoPane() {
        return levelInfoPane;
    }

    public static GameView getInstance() {
        return instance;
    }

    public static void setInstance(GameView view) {
        instance = view;
    }
}
