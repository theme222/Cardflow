package ui.card;

import component.card.Card;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import registry.render.RenderLayer;
import ui.render.Renderer;
import ui.render.RenderState;
import util.GridPos;

public class CardRenderer extends Renderer<Card> {

    public static final CardRenderer INSTANCE =
            new CardRenderer();

    private static final Font CARD_FONT =
            Font.font("Mozart NBP", 16);

    private CardRenderer() {}

    @Override
    public void render(Card card, Pane node, GridPos pos) {
        // draw the material
        RenderState matState = CardRenderResolver.resolveMaterial(card);
        RenderState suitState = CardRenderResolver.resolveSuit(card);
        RenderState valueState =  CardRenderResolver.resolveValue(card);

        Canvas canvas = new Canvas(matState.width(), matState.height());

        // Calling draw with canvas manually to allow multiple draws on top of each other
        drawWithCanvas(node, matState, canvas);
        drawWithCanvas(node, suitState, canvas);
        drawWithCanvas(node, valueState, canvas);

        node.getChildren().setAll(canvas);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.CARD;
    }
}
