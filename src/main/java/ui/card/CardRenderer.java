package ui.card;

import java.util.HashSet;
import java.util.Set;

import application.view.GameView;
import component.card.Card;
import event.EventListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.event.AfterMovementEvent;
import logic.movement.CardMovement;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import ui.render.Renderer;
import ui.game.GameRenderStack;
import ui.game.GameTilePane;
import ui.render.RenderState;
import util.GridPos;

public class CardRenderer extends Renderer<Card> {

    public static final CardRenderer INSTANCE = new CardRenderer();

    private static final double TILE_SIZE = 85;

    private static final Font CARD_FONT = Font.font("Mozart NBP", 16);

    public EventListener<AfterMovementEvent> movementListener = this::onMovementEvent;

    private Set<Card> animatingCards = new HashSet<>();

    private CardRenderer() {
        
    }

    @Override
    protected double tileSize() {
        return TILE_SIZE;
    }

    @Override
    public void render(Card card, Pane node, GridPos pos, boolean animating) {
        if(animatingCards.contains(card) && !animating) return; // skip rendering if animating to avoid conflicts
        
        RenderState state = CardRenderResolver.resolve(card);

        // draw base card via shared renderer
        draw(node, state);

        // 🔹 overlay text manually (for now)
        Canvas canvas = (Canvas) node.getChildren().get(0);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFont(CARD_FONT);
        gc.setFill(Color.BLACK);

        String text = card.getSuit() + "\n" +
                card.getValue() + "\n" +
                card.getMaterial();

        gc.fillText(text, 0, 10);
    }

    public void onAnimationComplete(Card card, GridPos from, GridPos to) {
        animatingCards.remove(card);
        GameView.getInstance().updateTileAndAdjacent(from);
        GameView.getInstance().updateTileAndAdjacent(to);
    }

    public void onMovementEvent(AfterMovementEvent event) {
        for (CardMovement movement : event.getMovements()) {
            if (movement.card() != null) {
                animatingCards.add(movement.card());
                CardMovementAnimation.INSTANCE.animate(movement.card(), movement.from(), movement.to());
            }
        }
    }

    @Override
    public RenderLayer layer() {
        return RenderLayer.CARD;
    }
}
