package application;

/**
 * Defines the available animation styles for switching between {@link application.view.View}s.
 * <p>
 * These constants are used by the {@link ViewManager} to determine the visual 
 * effect applied to the scene graph during a view change.
 */
public enum TransitionType {
    NONE,
    FADE,
    ZOOM
}