package ui.util;

import javafx.scene.image.Image;

public final class Sprite {
    public final Image image;
    public final double rotationOffset;
    public final boolean mirrorX;

    public Sprite(Image image, double rotationOffset, boolean mirrorX) {
        this.image = image;
        this.rotationOffset = rotationOffset;
        this.mirrorX = mirrorX;
    }
}
