package application;

import java.io.IOException;
import java.util.Objects;

import application.controller.PlacementController;
import application.view.GameView;
import application.view.LevelSelectorView;
import application.view.MainMenuView;
import audio.AudioManager;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import logic.GameLevel;
import util.LevelLoader;

/**
 * The {@code Game} class is the primary orchestrator for the application's lifecycle.
 * <p>
 * It handles the initialization of the primary stage, the bootstrapping of game 
 * registries, and the setup of the {@link ViewManager}. It also defines global 
 * keyboard shortcuts that persist across the application.
 */
public final class Game {

    /** * Initializes the game environment and sets up the primary window.
     * <p>
     * This method performs the following actions:
     * <ul>
     * <li>Calls {@link GameBootstrap#init()} to prepare registries and event listeners.</li>
     * <li>Configures the primary stage to shut down both JavaFX and the JVM on close.</li>
     * <li>Initializes the {@link ViewManager} with the {@link MainMenuView}.</li>
     * <li>Registers global key listeners (e.g., Backspace for Sandbox mode).</li>
     * </ul>
     * * @param primaryStage The main window stage provided by the JavaFX Application thread.
     */
    public static void init(Stage primaryStage) {
        // Initialize the registries and events necessary for the engine to run
        GameBootstrap.init(); 

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        // Initialize the ViewManager and set the initial landing view
        ViewManager.init(primaryStage, new MainMenuView());
        ViewManager managerInstance = ViewManager.getInstance();

        /**
         * Global Key Listener
         * <p>
         * If the user presses BACK_SPACE while in the LevelSelectorView, 
         * the game transitions to a hidden "sandbox" level.
         */
        managerInstance.scene.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.BACK_SPACE) {
                // Ensure this shortcut only works from the Level Selector screen
                if (!managerInstance.currentViewIs(LevelSelectorView.class)) return;
                
                try {
                    GameLevel sandbox = LevelLoader.loadLevel("sandbox");
                    managerInstance.switchView(new GameView(sandbox), TransitionType.FADE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        primaryStage.show();
    }

}