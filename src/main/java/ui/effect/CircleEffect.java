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

public class CircleEffect extends Circle {

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
