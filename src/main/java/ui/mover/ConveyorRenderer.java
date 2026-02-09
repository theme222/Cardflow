package ui.mover;

import component.mover.Conveyor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import ui.mover.helper.ConveyorTopology;
import ui.render.Renderer;
import ui.util.Sprite;
import util.GridPos;

public class ConveyorRenderer extends Renderer<Conveyor> {

    public static final ConveyorRenderer INSTANCE = new ConveyorRenderer();

    private static final double TILE_SIZE = 85;

    private static final Image CONVEYOR_BASE_IMAGE = new Image(
            ConveyorRenderer.class.getResourceAsStream(
                    "/asset/tiles/mover/conveyor/conveyor-base.png"),
            0, 0, true, false);

    private static final Image CONVEYOR_TURN_RIGHT_IMAGE = new Image(
            ConveyorRenderer.class.getResourceAsStream(
                    "/asset/tiles/mover/conveyor/conveyor-turn-right.png"),
            0, 0, true, false);

    private ConveyorRenderer() {
    }

    @Override
    public void render(Conveyor conveyor, Pane node, GridPos pos) {
        Canvas canvas = new Canvas(TILE_SIZE, TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, TILE_SIZE, TILE_SIZE);

        Sprite sprite = selectSprite(conveyor, pos);
        double rotation = rotationFor(conveyor) + sprite.rotationOffset;

        drawRotated(gc, sprite.image, rotation, sprite.mirrorX);

        node.getChildren().setAll(canvas);
    }

    /** Choose which sprite to draw (base vs turn-left, etc.) */
    private Sprite selectSprite(Conveyor conveyor, GridPos pos) {
        return switch (ConveyorTopology.resolve(conveyor, pos)) {
            case TURN_RIGHT ->
                new Sprite(CONVEYOR_TURN_RIGHT_IMAGE, -90, false);

            case TURN_LEFT ->
                new Sprite(CONVEYOR_TURN_RIGHT_IMAGE, +90, true);

            default ->
                new Sprite(CONVEYOR_BASE_IMAGE, 0, false);
        };
    }

    /** Rotation angle in degrees */
    private double rotationFor(Conveyor conveyor) {
        return switch (conveyor.getDirection()) {
            case UP -> 0;
            case RIGHT -> 90;
            case DOWN -> 180;
            case LEFT -> 270;
            default -> throw new IllegalArgumentException("Unexpected value: " + conveyor.getDirection());
        };
    }

    /** Draw image rotated around tile center */
    private void drawRotated(GraphicsContext gc, Image image, double angle, boolean mirrorX) {
        gc.save();

        gc.translate(TILE_SIZE / 2, TILE_SIZE / 2);
        gc.rotate(angle);

        if (mirrorX) {
            gc.scale(-1, 1); // mirror horizontally
        }

        gc.drawImage(
                image,
                -TILE_SIZE / 2,
                -TILE_SIZE / 2,
                TILE_SIZE,
                TILE_SIZE);

        gc.restore();
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.MOVER;
    }
}
