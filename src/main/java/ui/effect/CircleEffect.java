package ui.effect;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import util.Config;
import util.GridPos;

/**
 * A visual effect that displays a glowing circle on a grid tile.
 * <p>
 * The effect appears at the center of a tile, scales down, and fades out
 * over a short duration.
 * </p>
 *
 * This is typically used to highlight tile interactions such as placement,
 * selection, or other transient events.
 */
public class CircleEffect extends Circle {

    /**
     * Creates a circular visual effect at the given grid position.
     *
     * @param c   the color of the effect
     * @param pos the grid position where the effect will appear
     */
    public CircleEffect(Color c, GridPos pos) {
        super(10);

        // Create glow circle
        this.setFill(c);
        this.setOpacity(0.8);

        double centerX = pos.getX() * Config.TILE_SIZE + Config.TILE_SIZE / 2.0;
        double centerY = pos.getY() * Config.TILE_SIZE + Config.TILE_SIZE / 2.0;

        this.setTranslateX(centerX);
        this.setTranslateY(centerY);
    }

    /**
     * Creates the animation used by this effect.
     * <p>
     * The animation scales the circle down while fading its opacity to zero.
     * Both animations run simultaneously.
     * </p>
     *
     * @return a transition that plays the circle's scale and fade animation
     */
    public Transition getTransition() {
        // Scale animation
        ScaleTransition scale = new ScaleTransition(Duration.millis(250), this);
        scale.setFromX(4);
        scale.setFromY(4);
        scale.setToX(1);
        scale.setToY(1);

        // Fade animation
        FadeTransition fade = new FadeTransition(Duration.millis(250), this);
        fade.setFromValue(0.8);
        fade.setToValue(0);

        return new ParallelTransition(scale, fade);
    }
}
