package application;

import java.util.Set;

import component.GameTile;
import component.mover.Conveyor;
import component.mover.Mover;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import util.GridPos;

public final class Game {

    private static final GameController controller = new GameController();

    public static void init(Stage primaryStage) {
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });

        SceneManager.init(primaryStage);
        SceneManager.getInstance().showLevelSelector();
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
}
