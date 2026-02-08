package application;

import application.scene.GameScene;
import application.scene.LevelSelectorScene;
import application.scene.MainMenuScene;
import javafx.application.Platform;
import logic.GameLevel;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static final String[] CSS_FILES= {"base.css", "cards.css", "tiles.css", "ui.css"};
    private static SceneManager instance;
    private final Stage stage;

    public static SceneManager getInstance() {
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    public static void init(Stage stage) {
        instance = new SceneManager(stage);
    }

    private SceneManager(Stage stage) {
        this.stage = stage;
        stage.setTitle("Cardflow");
    }

    public void setScene(Scene scene) {
        for (String cssFile : CSS_FILES) {
            scene.getStylesheets().add(getClass().getResource("/css/"+cssFile).toExternalForm());
        }

        stage.setScene(scene);
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }

    public void showMainMenu() {
        this.setScene(MainMenuScene.create());
    }

    public void showLevelSelector() {
        this.setScene(LevelSelectorScene.create());
    }

    public void showGame(GameLevel level) {
        this.setScene(GameScene.create(level));
    }
}
