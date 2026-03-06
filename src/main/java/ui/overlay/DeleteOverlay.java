package ui.overlay;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import util.Config;

public class DeleteOverlay {
    public static final DeleteOverlay INSTANCE = new DeleteOverlay();

    private static final double BORDER_WIDTH = 3.0;

    private static final double BORDER_OPACITY = 0.6;
    private static final double FILL_OPACITY = 0.18;

    /** 
     * @param overlayPane
     */
    public void render(Pane overlayPane) {

        double tileSize = Config.TILE_SIZE;

        Canvas canvas = new Canvas(tileSize, tileSize);
        canvas.setMouseTransparent(true);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        double inset = BORDER_WIDTH / 2.0;

        gc.save();

        // ===== Fill (very transparent red) =====
        gc.setGlobalAlpha(FILL_OPACITY);
        gc.setFill(Color.RED);
        gc.fillRect(
                inset,
                inset,
                tileSize - BORDER_WIDTH,
                tileSize - BORDER_WIDTH
        );

        // ===== Border (stronger red) =====
        gc.setGlobalAlpha(BORDER_OPACITY);
        gc.setStroke(Color.RED);
        gc.setLineWidth(BORDER_WIDTH);
        gc.strokeRect(
                inset,
                inset,
                tileSize - BORDER_WIDTH,
                tileSize - BORDER_WIDTH
        );

        gc.restore();

        overlayPane.getChildren().setAll(canvas);
    }
}