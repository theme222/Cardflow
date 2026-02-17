package application;

import java.io.IOException;
import java.util.Set;

import application.view.GameView;
import application.view.LevelSelectorView;
import application.view.MainMenuView;
import component.GameTile;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import logic.GameLevel;
import placement.PlacementController;
import util.GridPos;
import util.LevelLoader;

public final class Game {

    private static final PlacementController controller = new PlacementController();

    public static void init(Stage primaryStage) {
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        ViewManager.init(primaryStage, new MainMenuView());
        ViewManager managerInstance = ViewManager.getInstance();

        managerInstance.scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case BACK_SPACE: {
                    if (!managerInstance.currentViewIs(LevelSelectorView.class)) return;
                    try {
                        GameLevel sandbox = LevelLoader.loadSandboxLevel();
                        managerInstance.switchView(new GameView(sandbox), TransitionType.FADE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case SPACE: {
                    if (!managerInstance.currentViewIs(GameView.class)) return;
                    GameLevel.getInstance().doTick();
                    for (GridPos point : GameLevel.getInstance().changedPoints) {
                        GameView.getInstance().getGameGridTilePanes()[point.getY()][point.getX()].updateUI();
                    }
                }
            }
        });


        managerInstance.scene.onMouseClickedProperty().set(event -> {
            Game.onSceneClick(
                event.getButton(),
                event.isShiftDown(),
                event.isControlDown()
            );
        });

        primaryStage.show();
    }

    public static Set<GridPos> onTileClick(
            GameTile tile,
            MouseButton button,
            boolean shift,
            boolean ctrl
    ) {
        return controller.handleTileClick(tile, button, shift, ctrl);
    }

	public static void onSceneClick(
            MouseButton button,
            boolean shift,
            boolean ctrl
    ) {
        controller.handleSceneClick(button, shift, ctrl);
    }
}
