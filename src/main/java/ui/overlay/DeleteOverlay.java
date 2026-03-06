package ui.overlay;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import util.Config;

/**
 * The {@code DeleteOverlay} class provides a visual indicator for tiles 
 * marked for deletion.
 * <p>
 * It renders a semi-transparent red rectangle with a thicker red border 
 * over the target grid cell.
 */
public class DeleteOverlay {
    /** The singleton instance of the overlay. */
    public static final DeleteOverlay INSTANCE = new DeleteOverlay();

    private static final double BORDER_WIDTH = 3.0;

    private static final double BORDER_OPACITY = 0.6;
    private static final double FILL_OPACITY = 0.18;

    /** 
     * Renders the deletion indicator onto the provided pane.
     * @param overlayPane The pane representing the grid cell to overlay.
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