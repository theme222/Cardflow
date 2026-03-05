package application;

import java.io.IOException;
import java.util.Set;

import application.controller.PlacementController;
import application.view.GameView;
import application.view.LevelSelectorView;
import application.view.MainMenuView;
import audio.AudioManager;
import component.GameTile;
import engine.GameState;
import engine.TickEngine;
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

    public static void onSceneClick(
            MouseButton button,
            boolean shift,
            boolean ctrl
    ) {
        if (button == MouseButton.SECONDARY) {
            PlacementController.INSTANCE.handleRightClick();
            GameView.getInstance().getLevelInfoPane().updateInventoryUI();
            AudioManager.playSoundEffect("mover-rotate");
        }
    }
}
