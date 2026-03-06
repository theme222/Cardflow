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

/**
 * The {@code MainMenuView} class represents the initial landing screen of the application.
 * <p>
 * It displays the game title, project credits, and primary navigation buttons 
 * (Play and Exit). This view facilitates the transition into the level selection 
 * screen and handles graceful application termination.
 */
public class MainMenuView extends View {

    /** The singleton instance of the MainMenuView. */
    private static MainMenuView instance;

    /**
     * Constructs the MainMenuView and initializes its UI components.
     * <p>
     * The layout consists of:
     * <ul>
     * <li>A title {@link Label} using the "text-title" style class.</li>
     * <li>A subtitle {@link Label} for project information.</li>
     * <li>An {@link HBox} containing the "Play" and "Exit" buttons.</li>
     * </ul>
     * The "Play" button triggers a {@link TransitionType#ZOOM} transition to 
     * the {@link LevelSelectorView}.
     */
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

    /**
     * Executed when the view is switched to. 
     * Plays the main menu background music via the {@link AudioManager}.
     */
    @Override
    public void startup() {
        AudioManager.playMusic("music-menu");
    }

    /**
     * Performs necessary cleanup when the view is removed from the active scene.
     */
    @Override
    public void cleanup() {}

    /** * Gets the current instance of the MainMenuView.
     * @return the {@code MainMenuView} instance.
     */
    public static MainMenuView getInstance() {
        return instance;
    }

    /** * Sets the global instance for the MainMenuView.
     * @param view the view instance to register.
     */
    public static void setInstance(MainMenuView view) {
        instance = view;
    }
}