package application.view;

import javafx.scene.layout.StackPane;

/**
 * The {@code View} class serves as the abstract base for all visual screens in the application.
 * <p>
 * It provides a standard lifecycle for views, separating the initial construction from 
 * active state management. This architecture ensures that views can be cached and reused 
 * without re-instantiation, while still managing resource cleanup and event registration.
 */
public abstract class View {

    /** * The container for all visual elements in this view. 
     * Using a {@link StackPane} allows for easy layering of UI elements and overlays.
     */
    protected StackPane root;

    /**
     * Initializes a new View and sets up the root {@link StackPane}.
     */
    public View() {
        setRoot(new StackPane());
    }

    /**
     * Called every time the view becomes the active screen in the application.
     * <p>
     * This should be used to play music, reset timers, or refresh data. 
     * Unlike the constructor, this method is called even if the view was already 
     * instantiated (e.g., when navigating back).
     */
    public abstract void startup();

    /**
     * Called when the view is being navigated away from or destroyed.
     * <p>
     * <b>Critical:</b> Use this method to unregister event listeners, stop animations, 
     * or clear static references to prevent memory leaks.
     */
    public abstract void cleanup();

    /** * Returns the root container of this view.
     * @return the {@link StackPane} serving as the UI root.
     */
    public StackPane getRoot() { return root; }

    /** * Manually sets the root container for this view.
     * @param root the new {@link StackPane} to serve as the root.
     */
    public void setRoot(StackPane root) { this.root = root; }
}