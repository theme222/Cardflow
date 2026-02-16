package application;

import application.view.View;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Stack;

public class ViewManager { // Switching views instead of switching scenes to allow for custom transitions.
    private static final String[] CSS_FILES= {"base.css", "cards.css", "tiles.css", "ui.css"};
    private static ViewManager instance;
    public final Stage stage;
    public final Scene scene;
    public final StackPane sceneRoot;
    private final Stack<View> viewStack; // Used to automatically track where we have come from and also prevent regen of scenes

    public static ViewManager getInstance() {
        return instance;
    }

    public static void init(Stage stage) {
        instance = new ViewManager(stage);
    }

    private ViewManager(Stage stage) {
        this.stage = stage;
        stage.setTitle("Cardflow");

        this.sceneRoot = new StackPane();

        this.scene = new Scene(sceneRoot);

        for (String cssFile : CSS_FILES) {
            scene.getStylesheets().add(getClass().getResource("/css/"+cssFile).toExternalForm());
        }

        this.viewStack = new Stack<>();
        stage.setScene(scene);
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }

    private void normalizeView(View view) { // remove any and all effects of transitions
        StackPane root = view.getRoot();
        root.setScaleX(1.0);
        root.setScaleY(1.0);
        root.setOpacity(1);
    }

    public void transitionSwitchView(View newView) {
        // Switches view without transitions or anything
        normalizeView(newView);
        if (getCurrentView() != null)
            sceneRoot.getChildren().remove(getCurrentView().getRoot());

        setCurrentView(newView);
        sceneRoot.getChildren().add(newView.getRoot());
    }

    public void transitionFadeView(View newView) {
        normalizeView(newView);
        if (getCurrentView() == null) { // Doesn't have anything to transition from
            transitionSwitchView(newView);
            return;
        }

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), getCurrentView().getRoot());
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> {
            setCurrentView(newView);
            sceneRoot.getChildren().remove(getCurrentView().getRoot());
            sceneRoot.getChildren().add(newView.getRoot());

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newView.getRoot());
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        fadeOut.play();
    }

    public void transitionZoomView(View newView) {
        normalizeView(newView);
        if (getCurrentView() == null) { // Doesn't have anything to transition from
            transitionSwitchView(newView);
            return;
        }

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), getCurrentView().getRoot());
        scaleUp.setFromX(1);
        scaleUp.setFromY(1);
        scaleUp.setToX(40);
        scaleUp.setToY(40);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), getCurrentView().getRoot());
        fadeOut.setToValue(0);

        ParallelTransition pt = new ParallelTransition(scaleUp, fadeOut);

        pt.play();
        pt.setOnFinished(event -> {
            setCurrentView(newView);
            sceneRoot.getChildren().remove(getCurrentView().getRoot());
            sceneRoot.getChildren().add(newView.getRoot());

            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(300), newView.getRoot());
            scaleUp.setFromX(40);
            scaleUp.setFromY(40);
            scaleUp.setToX(1);
            scaleUp.setToY(1);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newView.getRoot());
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ParallelTransition pt2 = new ParallelTransition(scaleDown, fadeIn);
            pt2.play();
        });
    }

    // Internally it's a stack (but we don't need to let the users know that :p)
    public View getCurrentView() {
        if (viewStack.isEmpty()) return null;
        return viewStack.peek();
    }

    public View getPreviousView() { // It's a stack what do you expect
        viewStack.pop();
        return getCurrentView();
    }

    public boolean currentViewIs(Class<? extends View> viewClass) {
        return viewClass.isInstance(getCurrentView());
    }

    public void setCurrentView(View currentView) {
        viewStack.push(currentView);
    }
}
