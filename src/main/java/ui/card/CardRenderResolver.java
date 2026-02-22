package ui.card;

import component.card.Card;
import javafx.scene.image.Image;
import ui.render.RenderResolver;
import ui.render.RenderState;

import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

public final class CardRenderResolver extends RenderResolver {
    // Longer than GTA V load times

    private static class MaterialImage {

        private static final String RESOURCE_DIR = "/asset/card/material/";
        private static final String[] FILENAMES = {"glass", "metal", "plastic", "rubber", "stone"};
        private static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }

    }

    private static class SuitImage {

        private static final String RESOURCE_DIR = "/asset/card/suit/";
        private static final String[] FILENAMES = { "club", "heart", "diamond", "spade"};
        private static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }

    }

    private static class ValueImage {
        private static final String RESOURCE_DIR = "/asset/card/value/";
        private static final String[] FILENAMES = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k"};
        private static final Map<String, Image> blackImages = new HashMap<>(); // _Black
        private static final Map<String, Image> whiteImages = new HashMap<>(); // _White

        static { // Is it better to load case by case and cache it like that? maybe. Do I care? no.
            loadImageFiles(RESOURCE_DIR, FILENAMES, blackImages, "_black.png");
            loadImageFiles(RESOURCE_DIR, FILENAMES, whiteImages, "_white.png");
        }

    }

    private static final double CARD_WIDTH  = 50;
    private static final double CARD_HEIGHT = 70;

    private CardRenderResolver() {}

    public static RenderState resolveMaterial(Card card) {

        Image toRender = MaterialImage.images.getOrDefault(
            card.getMaterial().toString().toLowerCase(),
            MaterialImage.images.get("plastic")
        );

        return new RenderState(toRender, CARD_WIDTH, CARD_HEIGHT, 0, false, card.getMaterial() == Card.Material.GLASS ? 0.5: 1.0);
    }

    public static RenderState resolveSuit(Card card) {

        Image toRender = SuitImage.images.getOrDefault(
                card.getSuit().toString().toLowerCase(),
                MaterialImage.images.get("spade")
        );

        return new RenderState(toRender, CARD_WIDTH, CARD_HEIGHT, 0, false, 1.0);
    }

    public static RenderState resolveValue(Card card) {

        boolean useWhite = true; // TODO idk man it kinda just looks better with white all the time

        String valueString = String.valueOf(card.getValue());
        if (valueString.equals("1")) valueString = "a";
        else if (valueString.equals("11")) valueString = "j";
        else if (valueString.equals("12")) valueString = "q";
        else if (valueString.equals("13")) valueString = "k";

        Image toRender = (useWhite ? ValueImage.whiteImages: ValueImage.blackImages).getOrDefault(
            valueString,
            ValueImage.whiteImages.get("1")
        );

        return new RenderState(toRender, CARD_WIDTH, CARD_HEIGHT, 0, false, 1.0);
    }

}
