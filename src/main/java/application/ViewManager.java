package application;

import application.view.View;
import audio.AudioManager;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.CacheHint;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.shader.GlowOverlay;

import java.util.Stack;


public class ViewManager { // Switching views instead of switching scenes to allow for custom transitions.
    private static final String[] CSS_FILES= {"base.css", "text.css", "border.css", "button.css", "cards.css", "tiles.css", "ui.css"};
    private static ViewManager instance;
    public final Stage stage;
    public final Scene scene;
    public final StackPane sceneRoot;
    public final StackPane viewRoot;
    public final StackPane shaderRoot; // on top of view root
    private final Stack<View> viewStack; // Used to automatically track where we have come from and also prevent regen of scenes
    private final ViewTransition viewTransition;

    public class ViewTransition {

        public void transitionView(View oldView, View newView, TransitionType transitionType) {
            normalizeView(oldView);
            normalizeView(newView);
            switch (transitionType) {
                case NONE:
                    transitionNoneView(oldView, newView);
                    break;
                case FADE:
                    transitionFadeView(oldView, newView);
                    break;
                case ZOOM:
                    transitionZoomView(oldView, newView);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid transition type");
            }

        }

        private void normalizeView(View view) { // remove any and all effects of transitions
            StackPane root = view.getRoot();
            root.setScaleX(1.0);
            root.setScaleY(1.0);
            root.setOpacity(1);
        }

        private void transitionNoneView(View prevView, View newView) { // not my best naming skills :/
            // Switches view without transitions or anything
            viewRoot.getChildren().remove(prevView.getRoot());
            viewRoot.getChildren().add(newView.getRoot());
            resizeToCurrentView();
        }

        private void transitionFadeView(View prevView, View newView) {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), prevView.getRoot());
            fadeOut.setToValue(0);

            fadeOut.setOnFinished(e -> {
                viewRoot.getChildren().remove(prevView.getRoot());
                viewRoot.getChildren().add(newView.getRoot());

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newView.getRoot());
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
                resizeToCurrentView();
            });

            fadeOut.play();
            
        }

        private void transitionZoomView(View prevView, View newView) {
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), prevView.getRoot());
            scaleUp.setFromX(1);
            scaleUp.setFromY(1);
            scaleUp.setToX(40);
            scaleUp.setToY(40);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), prevView.getRoot());
            fadeOut.setToValue(0);

            ParallelTransition pt = new ParallelTransition(scaleUp, fadeOut);

            pt.play();
            pt.setOnFinished(event -> {
                viewRoot.getChildren().remove(prevView.getRoot());
                viewRoot.getChildren().add(newView.getRoot());

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
    }

    public static ViewManager getInstance() {
        return instance;
    }

    public static void init(Stage stage, View initialView) {
        instance = new ViewManager(stage, initialView);
    }

    private ViewManager(Stage stage, View initialView) {
        this.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setMaximized(true);
        stage.setTitle("Cardflow");


        this.viewRoot = new StackPane();

        this.shaderRoot = new StackPane();
        shaderRoot.getChildren().add(new GlowOverlay());
        shaderRoot.setMouseTransparent(true);

        this.sceneRoot = new StackPane();

        sceneRoot.getChildren().addAll(viewRoot, shaderRoot);

        this.scene = new Scene(sceneRoot);

        for (String cssFile : CSS_FILES) {
            scene.getStylesheets().add(getClass().getResource("/css/"+cssFile).toExternalForm());
        }

        this.viewStack = new Stack<>();
        this.viewTransition = new ViewTransition();
        initialView.startup();
        viewRoot.getChildren().add(initialView.getRoot());
        viewStack.push(initialView);

        stage.setScene(scene);
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }

    public void switchView(View newView, TransitionType transitionType) {
        getCurrentView().cleanup();
        newView.startup();
        viewTransition.transitionView(getCurrentView(), newView, transitionType);
        viewStack.push(newView);
    }

    public void switchViewReplace(View newView, TransitionType transitionType) {
        // Replace the top most view with this view. (Example: Next level button)
        getCurrentView().cleanup();
        newView.startup();
        viewTransition.transitionView(getCurrentView(), newView, transitionType);
        if (!viewStack.isEmpty()) viewStack.pop();
        viewStack.push(newView);
    }

    public boolean switchToPreviousView(TransitionType transitionType) {
        if (viewStack.size() <= 1) return false; // Can't really go back if theres nothing to go back to

        View oldView = viewStack.pop();
        View newView = viewStack.peek();
        oldView.cleanup();
        newView.startup();

        viewTransition.transitionView(oldView, newView, transitionType);
        return true;
    }

    // Internally it's a stack (but we don't need to let the users know that :p)
    public View getCurrentView() {
        if (viewStack.isEmpty()) return null;
        return viewStack.peek();
    }

    public boolean currentViewIs(Class<? extends View> viewClass) {
        return viewClass.isInstance(getCurrentView());
    }

    private void resizeToCurrentView() {
        Platform.runLater(stage::sizeToScene);
    }

}
