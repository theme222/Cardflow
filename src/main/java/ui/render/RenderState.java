package ui.render;

import javafx.scene.image.Image;

/**
 * A record representing the visual state of a component at a specific moment.
 * Contains image data, transformations, and effects used by the rendering system.
 * 
 * @param image The image to be rendered.
 * @param width The target width.
 * @param height The target height.
 * @param offsetX Horizontal offset from the tile center.
 * @param offsetY Vertical offset from the tile center.
 * @param rotationDeg Rotation in degrees.
 * @param mirrorX Whether to flip the image horizontally.
 * @param grayscale Whether to apply a grayscale filter.
 * @param alpha Transparency level (0.0 to 1.0).
 */
public record RenderState(
        Image image,
        double width,
        double height,
        double offsetX,
        double offsetY,
        double rotationDeg,
        boolean mirrorX,
        boolean grayscale,
        double alpha
) {}
