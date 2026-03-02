package ui.modifier.changer;

import component.modifier.changer.*;
import javafx.scene.image.Image;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.Config;
import util.GridPos;

import java.util.HashMap;
import java.util.Map;

public final class SetterRenderResolver extends RenderResolver {

    private static class SetterImage {
        private static final String[] SUIT_FILENAMES = {"modify-club", "modify-diamond", "modify-heart", "modify-spade"};
        private static final String[] MATERIAL_FILENAMES = {"modify-plastic", "modify-metal", "modify-glass", "modify-stone", "modify-rubber", "modify-corrupted"};

        public static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles( "/asset/tiles/modifier/changer/suit/", SUIT_FILENAMES, images, ".png");
            loadImageFiles( "/asset/tiles/modifier/changer/material/", MATERIAL_FILENAMES, images, ".png");

            // Base image for number setter
            images.put("modify-base", new Image(
                    SetterRenderResolver.class.getResourceAsStream("/asset/tiles/modifier/modify-base.png"),
                    0, 0, true, false
            ));
        }
    }


    private SetterRenderResolver() {}

    public static RenderState resolve(
            Setter<?> setter,
            GridPos pos,
            double alpha
    ) {
        Image symbol;
        if (setter instanceof ValueSetter)
            symbol = SetterImage.images.get("modify-base");
        else // Suit & Material Setter
            symbol = SetterImage.images.get("modify-" + setter.getChange().toString().toLowerCase()); // it is simply a skill issue if this doesn't work

        return new RenderState(
                symbol,
                Config.TILE_SIZE,
                Config.TILE_SIZE,
                0,
                0,
                0,
                false,
                alpha
        );
    }


}
