package ui.shader;

import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class GlowOverlay extends Rectangle {
    // Because my laptop is genuinely too slow to use actual shaders :/

    public GlowOverlay() {

        Rectangle glowOverlay = new Rectangle(1920, 1080);
        glowOverlay.setFill(new RadialGradient(
                0,           // focusAngle
                0,           // focusDistance
                0.5,         // centerX (0.5 = middle)
                0.5,         // centerY (0.5 = middle)
                0.8,         // radius (how far the "light" reaches before hitting the dark edge)
                true,        // proportional
                CycleMethod.NO_CYCLE,
                // Stop 0: The "Hot" center (Bright Neon/White)
                new Stop(0, Color.web("#ffffff", 0.4)),
                // Stop 0.5: The primary glow color (e.g., Purple)
                new Stop(0.5, Color.web("#7a2e8a", 0.3)),
                // Stop 1.0: The dark outer edge (Vignette)
                new Stop(1, Color.web("#000000", 0.7))
        ));

        glowOverlay.setBlendMode(BlendMode.ADD);
        glowOverlay.setOpacity(1); // Adjust for "intensity"
        glowOverlay.setMouseTransparent(true);
    }
}

