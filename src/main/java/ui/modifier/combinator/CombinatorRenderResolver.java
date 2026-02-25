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

public class CombinatorRenderResolver extends RenderResolver {

    private static class CombinatorImage {
        private static final String RESOURCE_DIR = "/asset/tiles/modifier/combinator/";
        private static final String[] FILENAMES = {"combinator-absorber", "combinator-duplicator", "combinator-merger", "combinator-splitter", "combinator-vaporizer"};

        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }
    }


    private CombinatorRenderResolver() {}

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
                false,
                alpha
        );
    }

}
