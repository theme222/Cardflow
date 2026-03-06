package ui.modifier.combinator;

import component.modifier.changer.Setter;
import component.modifier.changer.ValueSetter;
import component.modifier.combinator.Combinator;
import javafx.scene.image.Image;
import ui.modifier.changer.SetterRenderResolver;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.Config;
import util.GridPos;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the visual state of a {@link Combinator} modifier.
 * Handles different types like absorbers, duplicators, mergers, splitters, and vaporizers.
 */
public class CombinatorRenderResolver extends RenderResolver {

    /**
     * Inner class for loading combinator images.
     */
    private static class CombinatorImage {
        private static final String RESOURCE_DIR = "/asset/tiles/modifier/combinator/";
        private static final String[] FILENAMES = {"combinator-absorber", "combinator-duplicator", "combinator-merger", "combinator-splitter", "combinator-vaporizer"};

        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }


    /**
     * Private constructor to prevent instantiation.
     */
    private CombinatorRenderResolver() {}

    /** 
     * Resolves the render state for a combinator.
     * 
     * @param combinator The combinator instance.
     * @param pos The grid position.
     * @param alpha The transparency level.
     * @return A {@link RenderState} for the combinator.
     */
    public static RenderState resolve(
            Combinator combinator,
            GridPos pos,
            double alpha
    ) {
        Image symbol = CombinatorImage.images.get("combinator-"+combinator.getClass().getSimpleName().toLowerCase());

        return new RenderState(
                symbol,
                Config.TILE_SIZE,
                Config.TILE_SIZE,
                0,
                0,
                0,
                false,
                combinator.isDisabled(),
                alpha
        );
    }

}
