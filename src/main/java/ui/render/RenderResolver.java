package ui.render;

import component.mover.Mover;
import javafx.scene.image.Image;
import ui.card.CardRenderResolver;

import java.util.Map;

/**
 * Base class for resolvers that load and manage image assets for rendering.
 */
public abstract class RenderResolver {

    /** 
     * Loads a set of image files into a map.
     * 
     * @param prefix The directory prefix (e.g., "/asset/tiles/").
     * @param filenames An array of base filenames to load.
     * @param imageMap The map to store the loaded images in.
     * @param suffix The file extension suffix (e.g., ".png").
     */
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
