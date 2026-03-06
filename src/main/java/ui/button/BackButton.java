package ui.button;

import application.TransitionType;
import application.ViewManager;
import audio.AudioManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * The {@code BackButton} is a specialized UI component used for 
 * view navigation.
 * <p>
 * It interacts with the {@link ViewManager} to pop the current view 
 * off the navigation stack. If the navigation stack is empty (e.g., 
 * the user is on the Main Menu), the button triggers a clean 
 * application shutdown.
 */
public class BackButton extends VBox {

    /**
     * Constructs a {@code BackButton} with a "back" arrow symbol («).
     * <p>
     * The button is styled with the "round" CSS class and is configured 
     * to trigger a {@link TransitionType#FADE} animation upon clicking. 
     * It also triggers a directional sound effect via {@link AudioManager}.
     */
    public BackButton() {
        Button backButton = new Button("«");
        
        // UI Styling
        backButton.getStyleClass().add("round");
        backButton.setFocusTraversable(false);
        
        // Navigation Logic
        backButton.setOnAction(e -> {
            AudioManager.playSoundEffect("button-click");
            ViewManager manager = ViewManager.getInstance();
            
            /* * Attempt to revert to the previous screen. 
             * If no history exists, the application exits.
             */
            if (!manager.switchToPreviousView(TransitionType.FADE)) {
                Platform.exit();
            }
        });

        // Layout Configuration
        setPadding(new Insets(10));
        setPickOnBounds(false); // Allows clicking through the VBox container
        getChildren().addAll(backButton);
    }
}