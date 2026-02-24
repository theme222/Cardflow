package ui.render;

import component.mover.Mover;
import javafx.scene.image.Image;
import ui.card.CardRenderResolver;

import java.util.Map;

public abstract class RenderResolver {

    protected static void loadImageFiles(String prefix, String[] filenames, Map<String, Image> imageMap, String suffix) {
        // If you have time probably move imageMap to the first or last position :skull:
        // prefix = directory (usually) suffix = filetype (usually)
        for (String filename : filenames) {
            imageMap.put(
                filename,
                new Image(
                        CardRenderResolver.class.getResourceAsStream( prefix + filename + suffix),
                        0, 0, true, false
                )
            );
        }
    }

}
