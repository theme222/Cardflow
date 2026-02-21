package ui.card;

import component.card.Card;
import javafx.scene.image.Image;
import ui.render.RenderState;

public final class CardRenderResolver {

    private static final Image PLASTIC_CARD_IMAGE = new Image(
            CardRenderResolver.class.getResourceAsStream( "/asset/card/plastic-card.png"),
            0, 0,
            true,
            false
    );

    private static final Image STONE_CARD_IMAGE = new Image(
            CardRenderResolver.class.getResourceAsStream( "/asset/card/stone-card.png"),
            0, 0,
            true,
            false
    );

    private static final Image METAL_CARD_IMAGE = new Image(
            CardRenderResolver.class.getResourceAsStream( "/asset/card/metal-card.png"),
            0, 0,
            true,
            false
    );


    private static final double CARD_WIDTH  = 50;
    private static final double CARD_HEIGHT = 70;

    private CardRenderResolver() {}

    public static RenderState resolve(Card card) {
        Image toRender = switch (card.getMaterial()) {
            case PLASTIC -> PLASTIC_CARD_IMAGE;
            case STONE -> STONE_CARD_IMAGE;
            case METAL -> METAL_CARD_IMAGE;
            default -> PLASTIC_CARD_IMAGE;
        };
        return new RenderState(toRender, CARD_WIDTH, CARD_HEIGHT, 0, 0, 0, false, 1.0);
    }
}
