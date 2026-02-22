package ui.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import util.Config;

public abstract class Renderer<T> {

    protected abstract double tileSize();

    public abstract void render(T tile, Pane node, util.GridPos pos);

    protected void draw(Pane node, RenderState state) {
        Canvas canvas = new Canvas(state.width(), state.height());
        drawWithCanvas(node, state, canvas);
        node.getChildren().setAll(canvas);
    }

    protected void drawWithCanvas(Pane node, RenderState state, Canvas canvas) {
        double w = state.width();
        double h = state.height();

        double tile = tileSize();

        // 🔥 CENTER THE CANVAS IN THE TILE
        canvas.setLayoutX((tile - w) / 2.0);
        canvas.setLayoutY((tile - h) / 2.0);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setImageSmoothing(false);
        // gc.clearRect(0, 0, w, h);

        gc.save();
        gc.setGlobalAlpha(state.alpha());

        // rotate around renderable center
        gc.translate(w / 2, h / 2);
        gc.rotate(state.rotationDeg());

        if (state.mirrorX()) {
            gc.scale(-1, 1);
        }
        gc.drawImage(
                state.image(),
                -w / 2,
                -h / 2,
                w,
                h);

        gc.restore();
    }

    protected void textWithCanvas(Pane node, String text, RenderState renderState, Canvas canvas) { // thx chatgpt <3
        // RenderState is only used for width, height and alpha here. No image is read.
        GraphicsContext gc = canvas.getGraphicsContext2D();
        int fontSize = (int)(96 / Math.pow(2, text.length())); // rough sizing calculations
        Font font = Font.font(Config.MONOSPACE_FONT, fontSize);

        gc.save();
        gc.setGlobalAlpha(renderState.alpha());
        gc.setFont(font);
        gc.setTextAlign(TextAlignment.CENTER);

        Text temp = new Text(text);
        temp.setFont(font);

        // Centering the text. I have no idea what any of this means but it works ¯\_(ツ)_/¯
        double textHeight = temp.getLayoutBounds().getHeight();
        double baselineOffset = temp.getBaselineOffset();

        double x = renderState.width() / 2;
        double y = renderState.height() / 2 + (baselineOffset - textHeight / 2);

        gc.fillText(text, x, y);
        gc.restore();
    }

    public abstract registry.render.RenderLayer layer();
}
