package ui.card;

import component.card.Card;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import ui.render.Renderer;
import util.GridPos;

/**
 * Handles the visual animation of cards moving between grid positions.
 * Utilizes a floating layer to perform smooth transitions across tiles.
 */
public class CardMovementAnimation {

    /** Singleton instance of CardMovementAnimation. */
    public static final CardMovementAnimation INSTANCE = new CardMovementAnimation();

    /** Size of a single tile in pixels. */
    private static final double TILE_SIZE = 85;
    /** Duration of the movement animation. */
    private static final Duration MOVE_DURATION = Duration.millis(200);

    /** The pane used as a floating layer for animations. */
    private Pane floatingLayer;

    /** The render layer used for card animations. */
    private RenderLayer layer = RenderLayer.CARDANIM;

    /**
     * Constructs a new CardMovementAnimation.
     */
    public CardMovementAnimation() {}

    /** 
     * Performs a translation animation for a card from one grid position to another.
     * 
     * @param card The {@link Card} being moved.
     * @param from The starting {@link GridPos}.
     * @param to The destination {@link GridPos}.
     */
    public void animate(Card card, GridPos from, GridPos to) {
        floatingLayer = FloatingLayerRegistry.INSTANCE.getPane(layer);

        // Create temporary node
        Pane animatedNode = new Pane();
        animatedNode.setPrefSize(TILE_SIZE, TILE_SIZE);

        // Render card into it
        Renderer<Card> renderer = CardRenderer.INSTANCE;
        renderer.render(card, animatedNode, from, true);

        // Compute pixel positions
        double startX = from.getX() * TILE_SIZE;
        double startY = from.getY() * TILE_SIZE;

        double endX = to.getX() * TILE_SIZE;
        double endY = to.getY() * TILE_SIZE;

        animatedNode.setTranslateX(startX);
        animatedNode.setTranslateY(startY);
        

        // Add to floating layer
        floatingLayer.getChildren().add(animatedNode);

        // Animate
        TranslateTransition transition =
                new TranslateTransition(MOVE_DURATION, animatedNode);

        transition.setToX(endX);
        transition.setToY(endY);

        transition.setOnFinished(e -> {
            floatingLayer.getChildren().remove(animatedNode);
            CardRenderer.INSTANCE.onAnimationComplete(card,from, to);
        });

        transition.play();
    }
}
