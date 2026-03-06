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

/**
 * Renderer for {@link Card} components.
 * Handles composite rendering of card material, suit, and value, as well as shadows.
 */
public class CardRenderer extends Renderer<Card> {

    /** Singleton instance of CardRenderer. */
    public static final CardRenderer INSTANCE = new CardRenderer();

//    private static final Font CARD_FONT =
//            Font.font("Mozart NBP", 16);

    /** Listener for movement events to trigger animations. */
    public EventListener<AfterMovementEvent> movementListener = this::onMovementEvent;

    /** Set of cards currently undergoing animation. */
    private Set<Card> animatingCards = new HashSet<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private CardRenderer() {
        
    }

    /** 
     * Renders a card with standard centering and shadows.
     * 
     * @param card The card to render.
     * @param node The Pane to render into.
     * @param pos The grid position.
     * @param animating True if currently animating.
     */
    @Override
    public void render(Card card, Pane node, GridPos pos, boolean animating) {
        render(card, node, pos, animating, true, true);
    }

    /** 
     * Renders a card with configurable options for centering and shadows.
     * 
     * @param card The card to render.
     * @param node The Pane to render into.
     * @param pos The grid position.
     * @param animating True if currently animating.
     * @param centerToTile Whether to center the card within the tile.
     * @param showShadow Whether to draw a drop shadow under the card.
     */
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

    /** 
     * Callback invoked when a card movement animation is complete.
     * 
     * @param card The card that moved.
     * @param from The origin position.
     * @param to The target position.
     */
    public void onAnimationComplete(Card card, GridPos from, GridPos to) {
        animatingCards.remove(card);
        GameView.getInstance().updateTileAndAdjacent(from);
        GameView.getInstance().updateTileAndAdjacent(to);
    }

    /** 
     * Responds to an {@link AfterMovementEvent} by starting animations for all moving cards.
     * 
     * @param event The event data.
     */
    public void onMovementEvent(AfterMovementEvent event) {
        for (CardMovement movement : event.getMovements()) {
            if (movement.card() != null) {
                animatingCards.add(movement.card());
                CardMovementAnimation.INSTANCE.animate(movement.card(), movement.from(), movement.to());
            }
        }
    }

    /** 
     * Returns the render layer for cards.
     * 
     * @return {@link RenderLayer#CARD}.
     */
    @Override
    public RenderLayer layer() {
        return RenderLayer.CARD;
    }
}
