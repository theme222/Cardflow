package application.view;

import application.TransitionType;
import application.ViewManager;
import audio.AudioManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MainMenuView extends View {

    private static MainMenuView instance;

    public MainMenuView() {
        super();
        setInstance(this);

        Label title = new Label("Cardflow");
        title.getStyleClass().addAll("text-title");

        Label subtitle = new Label("Final Project for Programming Methodology CEDT Class");
        subtitle.getStyleClass().addAll("text-heading", "text-muted");


        Button playButton = new Button("Play ▶");
        playButton.getStyleClass().add("button-primary");
        playButton.setOnAction(event -> {
            AudioManager.playSoundEffect("button-click");
            ViewManager.getInstance().switchView(new LevelSelectorView(), TransitionType.ZOOM);
        });

        Button exitButton =  new Button("Exit");
        exitButton.getStyleClass().add("button-info");
        exitButton.setOnAction(event -> {
            AudioManager.playSoundEffect("button-click");
            Platform.exit();
        });

        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(20);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(playButton, exitButton);

        VBox layout = new VBox();

        layout.setSpacing(50);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(title, subtitle, buttonContainer);

        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(layout);
    }

    @Override
    public void startup() {
        AudioManager.playMusic("music-menu");
    }

    @Override
    public void cleanup() {}

    public static MainMenuView getInstance() {
        return instance;
    }

    public static void setInstance(MainMenuView view) {
        instance = view;
    }
}
