package ui.card;

import java.util.HashSet;

import component.card.Card;
import event.EventListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.event.AfterMovementEvent;
import logic.movement.CardMovement;
import registry.render.RenderLayer;
import ui.render.Renderer;
import ui.game.GameTilePane;
import ui.render.RenderState;
import util.GridPos;

public class CardRenderer extends Renderer<Card> {

    public static final CardRenderer INSTANCE =
            new CardRenderer();

    private static final double TILE_SIZE = 85;

    private static final Font CARD_FONT =
            Font.font("Mozart NBP", 16);

    private static HashSet<CardMovement> lastMovements = new HashSet<>();

    public EventListener onMovementTick;

    private CardRenderer() {}

    @Override
    protected double tileSize() {
        return TILE_SIZE;
    }

    @Override
    public void render(Card card, Pane node, GridPos pos) {
        RenderState state = CardRenderResolver.resolve(card);

        // draw base card via shared renderer
        draw(node, state);

        // 🔹 overlay text manually (for now)
        Canvas canvas = (Canvas) node.getChildren().get(0);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFont(CARD_FONT);
        gc.setFill(Color.BLACK);

        String text =
                card.getSuit() + "\n" +
                card.getValue() + "\n" +
                card.getMaterial();

        gc.fillText(text,0,10);
    }

    public void onMovementTick(AfterMovementEvent event) {
        // For now we just update all cards every movement tick, but ideally we should only update the ones that moved
        lastMovements = event.getMovements();
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.CARD;
    }
}
