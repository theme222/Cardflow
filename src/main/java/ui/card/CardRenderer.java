package ui.card;

import component.card.Card;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import registry.render.RenderLayer;
import ui.render.Renderer;
import util.GridPos;

public class CardRenderer extends Renderer<Card> {

    public static final CardRenderer INSTANCE = new CardRenderer();

    private static final double CARD_WIDTH = 50;
    private static final double CARD_HEIGHT = 70;

    private static final Image BASE_CARD_IMAGE = new Image(
            CardRenderer.class.getResourceAsStream(
                    "/asset/card/card-base.png"),
            0, 0,
            true,
            false);

    private static final Font CARD_FONT = Font.font("Monospaced", 12); // crisp + predictable

    private CardRenderer() {
    }

    @Override
    public void render(Card card, Pane node, GridPos pos) {
        Canvas canvas = new Canvas(CARD_WIDTH, CARD_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setImageSmoothing(false);
        gc.clearRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        // Base card
        gc.drawImage(BASE_CARD_IMAGE, 0, 0, CARD_WIDTH, CARD_HEIGHT);

        // Text overlay
        gc.setFont(CARD_FONT);
        gc.setFill(Color.BLACK);

        String text = card.toString();
        double padding = 6;
        double textY = CARD_HEIGHT - padding;

        gc.fillText(text, padding, textY);

        // 🔑 Center inside 85×85 tile
        double tileSize = 85;
        canvas.setLayoutX((tileSize - CARD_WIDTH) / 2);
        canvas.setLayoutY((tileSize - CARD_HEIGHT) / 2);

        node.getChildren().setAll(canvas);
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.CARD;
    }
}
