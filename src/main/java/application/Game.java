package application;

import java.io.IOException;
import java.util.Set;

import application.view.GameView;
import application.view.LevelSelectorView;
import application.view.MainMenuView;
import component.GameTile;
import event.Event;
import javafx.application.Platform;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import logic.GameLevel;
import logic.PlayerInventory;
import util.GridPos;
import util.LevelLoader;

public final class Game {

    public static void init(Stage primaryStage) {
        GameBootstrap.init(); // initialize the registries and events

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
                        GameLevel sandbox = LevelLoader.loadLevel("sandbox");
                        managerInstance.switchView(new GameView(sandbox), TransitionType.FADE);
                    } catch (IOException e) {
                        e.printStackTrace();
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
        // replacing most of this to calls to PlayerInventory to allow for dynamic selection.
        if (button == MouseButton.PRIMARY) {
            if (tile.getMover() == null)
                PlayerInventory.getInstance().placeToGrid(tile.getGridPos());
            else
                PlayerInventory.getInstance().removeFromGrid(tile.getGridPos());
        }

        if (button == MouseButton.SECONDARY) {
            if (tile.getMover() != null)
                tile.getMover().rotate();
            else
                PlayerInventory.getInstance().cycleRotation();
        }

        GameView.getInstance().getLevelInfoPane().updateInventoryUI(); // Not sure if this is the best place to put it
        GameView.getInstance().getTooltipLayer().updateTooltipInfo(new Event() { });
        return Set.of(tile.getGridPos());
    }

	public static void onSceneClick(
            MouseButton button,
            boolean shift,
            boolean ctrl
    ) {
        if (button == MouseButton.SECONDARY) {
            PlayerInventory.getInstance().cycleRotation();
            GameView.getInstance().getLevelInfoPane().updateInventoryUI();
        }
    }
}
