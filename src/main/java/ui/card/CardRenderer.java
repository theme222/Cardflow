package ui.card;

import java.util.HashSet;
import java.util.Set;

import application.view.GameView;
import component.card.Card;
import event.EventListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import logic.event.AfterMovementEvent;
import logic.movement.CardMovement;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import ui.render.Renderer;
import ui.game.GameRenderStack;
import ui.game.GameTilePane;
import ui.render.RenderState;
import util.Config;
import util.GridPos;

public class CardRenderer extends Renderer<Card> {

    public static final CardRenderer INSTANCE = new CardRenderer();

//    private static final Font CARD_FONT =
//            Font.font("Mozart NBP", 16);

    public EventListener<AfterMovementEvent> movementListener = this::onMovementEvent;

    private Set<Card> animatingCards = new HashSet<>();

    private CardRenderer() {
        
    }

    @Override
    public void render(Card card, Pane node, GridPos pos, boolean animating) {
        render(card, node, pos, animating, true, true);
    }

    public void render(Card card, Pane node, GridPos pos, boolean animating, boolean centerToTile, boolean showShadow) {
        // Center to Tile = false will give you the pane at the proper size.
        if(animatingCards.contains(card) && !animating) return; // skip rendering if animating to avoid conflicts
        node.getChildren().clear();

        // draw the material
        RenderState matState = CardRenderResolver.resolveMaterial(card);
        RenderState suitState = CardRenderResolver.resolveSuit(card);
        RenderState valueState =  CardRenderResolver.resolveValue(card);

        Canvas canvas = new Canvas(matState.width(), matState.height());

        // Calling draw with canvas manually to allow multiple draws on top of each other
        drawWithCanvas(node, matState, canvas, centerToTile);
        drawWithCanvas(node, suitState, canvas, centerToTile);
        drawWithCanvas(node, valueState, canvas, centerToTile);

        if (showShadow) {
            // Create the shadow shape
            Rectangle shadow = new Rectangle(matState.width(), matState.height());
            shadow.setFill(Color.rgb(0, 0, 0, 0.5)); // Black with 50% opacity

            if (centerToTile) {
                // 🔥 CENTER THE CANVAS IN THE TILE
                shadow.setLayoutX((Config.TILE_SIZE - matState.width()) / 2.0);
                shadow.setLayoutY((Config.TILE_SIZE - matState.height()) / 2.0);
            }

            shadow.setTranslateX(4); // Offset to the right
            shadow.setTranslateY(4); // Offset down

            shadow.setEffect(new GaussianBlur(4));
            node.getChildren().add(shadow);
        }

        node.getChildren().add(canvas);
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
