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

/**
 * The {@code ViewManager} coordinates all navigation and visual transitions within the application.
 * <p>
 * Instead of traditional JavaFX scene switching, this manager swaps the contents of a 
 * primary {@link StackPane}. This allows for complex transitions (Fade, Zoom) and 
 * the maintenance of a global "Shader" layer (e.g., {@link GlowOverlay}) that persists 
 * across different views.
 */
public class ViewManager {
    private static final String[] CSS_FILES= {"base.css", "text.css", "border.css", "button.css", "cards.css", "tiles.css", "ui.css"};
    private static ViewManager instance;
    
    public final Stage stage;
    public final Scene scene;
    public final StackPane sceneRoot;
    public final StackPane viewRoot;
    public final StackPane shaderRoot; 
    
    /** A history of visited views, allowing for "Back" navigation without re-instantiation. */
    private final Stack<View> viewStack; 
    private final ViewTransition viewTransition;
    private boolean isTransitioning;

    /**
     * Inner class responsible for executing JavaFX animations during view changes.
     */
    public class ViewTransition {

        /**
         * Orchestrates the movement from one view to another based on the requested transition type.
         * @param oldView The view currently being displayed.
         * @param newView The view to be displayed.
         * @param transitionType The animation style to apply.
         */
        public void transitionView(View oldView, View newView, TransitionType transitionType) {
            normalizeView(oldView);
            normalizeView(newView);
            switch (transitionType) {
                case NONE -> transitionNoneView(oldView, newView);
                case FADE -> transitionFadeView(oldView, newView);
                case ZOOM -> transitionZoomView(oldView, newView);
                default -> throw new IllegalArgumentException("Invalid transition type");
            }
        }

        /** Resets a view's visual properties to defaults (opacity 1, scale 1). */
        private void normalizeView(View view) {
            StackPane root = view.getRoot();
            root.setScaleX(1.0);
            root.setScaleY(1.0);
            root.setOpacity(1);
        }

        private void transitionNoneView(View prevView, View newView) {
            viewRoot.getChildren().remove(prevView.getRoot());
            viewRoot.getChildren().add(newView.getRoot());
            resizeToCurrentView();
        }

        private void transitionFadeView(View prevView, View newView) {
            isTransitioning = true;
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
                isTransitioning = false;
            });
            fadeOut.play();
        }

        private void transitionZoomView(View prevView, View newView) {
            isTransitioning = true;
            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), prevView.getRoot());
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
                scaleDown.setFromX(40);
                scaleDown.setFromY(40);
                scaleDown.setToX(1);
                scaleDown.setToY(1);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newView.getRoot());
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);

                ParallelTransition pt2 = new ParallelTransition(scaleDown, fadeIn);
                pt2.setOnFinished(event2 -> isTransitioning = false);
                pt2.play();
            });
        }
    }

    /** @return The singleton instance of the ViewManager. */
    public static ViewManager getInstance() {
        return instance;
    }

    /**
     * Bootstraps the ViewManager with the primary stage.
     * @param stage The JavaFX Stage.
     * @param initialView The first view to display (usually MainMenu).
     */
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
        shaderRoot.setMouseTransparent(true); // Ensures shaders don't block clicks

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
        isTransitioning = false;
    }

    /**
     * Navigates to a new view and pushes it onto the history stack.
     * @param newView The view to transition to.
     * @param transitionType The animation style.
     */
    public void switchView(View newView, TransitionType transitionType) {
        if (isTransitioning) return;
        getCurrentView().cleanup();
        newView.startup();
        viewTransition.transitionView(getCurrentView(), newView, transitionType);
        viewStack.push(newView);
    }

    /**
     * Replaces the current view with a new one without growing the stack.
     * Useful for linear progression (e.g., Level 1 -> Level 2).
     */
    public void switchViewReplace(View newView, TransitionType transitionType) {
        if (isTransitioning) return;
        getCurrentView().cleanup();
        newView.startup();
        viewTransition.transitionView(getCurrentView(), newView, transitionType);
        if (!viewStack.isEmpty()) viewStack.pop();
        viewStack.push(newView);
    }

    /**
     * Pops the current view and returns to the previous one in the stack.
     * @return {@code true} if navigation was possible.
     */
    public boolean switchToPreviousView(TransitionType transitionType) {
        if (isTransitioning || viewStack.size() <= 1) return false;

        View oldView = viewStack.pop();
        View newView = viewStack.peek();
        oldView.cleanup();
        newView.startup();

        viewTransition.transitionView(oldView, newView, transitionType);
        return true;
    }

    /** @return The view at the top of the stack. */
    public View getCurrentView() {
        return viewStack.isEmpty() ? null : viewStack.peek();
    }

    /** Checks if the current view is an instance of a specific class. */
    public boolean currentViewIs(Class<? extends View> viewClass) {
        return viewClass.isInstance(getCurrentView());
    }

    private void resizeToCurrentView() {
        Platform.runLater(stage::sizeToScene);
    }
}