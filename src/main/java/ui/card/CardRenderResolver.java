package ui.card;

import component.card.Card;
import component.card.Material;
import javafx.scene.image.Image;
import ui.render.RenderResolver;
import ui.render.RenderState;
import util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves the visual components of a {@link Card} into {@link RenderState} objects.
 * Handles loading and mapping images for card materials, suits, and values.
 */
public final class CardRenderResolver extends RenderResolver {

    /**
     * Inner class responsible for loading and storing images for card materials.
     */
    private static class MaterialImage {

        private static final String RESOURCE_DIR = "/asset/card/material/";
        private static final String[] FILENAMES = {"glass", "metal", "plastic", "rubber", "stone", "corrupted"};
        private static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }

    }

    /**
     * Inner class responsible for loading and storing images for card suits.
     */
    private static class SuitImage {

        private static final String RESOURCE_DIR = "/asset/card/suit/";
        private static final String[] FILENAMES = { "club", "heart", "diamond", "spade"};
        private static final Map<String, Image> images = new HashMap<>();

        static {
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }

    }

    /**
     * Inner class responsible for loading and storing images for card values.
     */
    private static class ValueImage {
        private static final String RESOURCE_DIR = "/asset/card/value/";
        private static final String[] FILENAMES = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k"};
        private static final Map<String, Image> images = new HashMap<>(); // _Black

        static { // Is it better to load case by case and cache it like that? maybe. Do I care? no.
            loadImageFiles(RESOURCE_DIR, FILENAMES, images, ".png");
        }

    }

    /** The standard width of a card in pixels. */
    private static final double CARD_WIDTH  = 50;
    /** The standard height of a card in pixels. */
    private static final double CARD_HEIGHT = 70;

    /**
     * Private constructor to prevent instantiation.
     */
    private CardRenderResolver() {}

    /** 
     * Resolves the material image for a card.
     * 
     * @param card The {@link Card} to resolve.
     * @return A {@link RenderState} containing the material image and properties.
     */
    public static RenderState resolveMaterial(Card card) {

        Image toRender = MaterialImage.images.getOrDefault(
            card.getMaterial().toString().toLowerCase(),
            MaterialImage.images.get("plastic")
        );

        return new RenderState(toRender, CARD_WIDTH, CARD_HEIGHT, 0, 0, 0, false, false, card.getMaterial() == Material.GLASS ? 0.5: 1.0);
    }

    /** 
     * Resolves the suit image for a card.
     * 
     * @param card The {@link Card} to resolve.
     * @return A {@link RenderState} containing the suit image.
     */
    public static RenderState resolveSuit(Card card) {

        Image toRender = SuitImage.images.getOrDefault(
                card.getSuit().toString().toLowerCase(),
                MaterialImage.images.get("spade")
        );

        return new RenderState(toRender, CARD_WIDTH, CARD_HEIGHT, 0, 0, 0, false, false, 1.0);
    }

    /** 
     * Resolves the value image for a card.
     * 
     * @param card The {@link Card} to resolve.
     * @return A {@link RenderState} containing the value image.
     */
    public static RenderState resolveValue(Card card) {

        Image toRender = ValueImage.images.getOrDefault(
            Util.getValueAsString(card.getValue()),
            ValueImage.images.get("1")
        );

        return new RenderState(toRender, CARD_WIDTH, CARD_HEIGHT, 0, 0, 0, false, false, 1.0);
    }

}
