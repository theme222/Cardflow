package ui.card;

import component.card.Card;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import ui.render.Renderer;
import util.GridPos;

public class CardMovementAnimation {

    public static final CardMovementAnimation INSTANCE = new CardMovementAnimation();

    private static final double TILE_SIZE = 85;
    private static final Duration MOVE_DURATION = Duration.millis(200);

    private Pane floatingLayer;

    private RenderLayer layer = RenderLayer.CARDANIM;

    public CardMovementAnimation() {}

    public void animate(Card card, GridPos from, GridPos to) {
        floatingLayer = FloatingLayerRegistry.INSTANCE.getPane(layer);

        //System.out.println("Animating card from " + from + " to " + to);

        // 1️⃣ Create temporary node
        Pane animatedNode = new Pane();

        //animatedNode.setStyle("-fx-border-color: red; -fx-border-width: 2;");
        
        animatedNode.setPrefSize(TILE_SIZE, TILE_SIZE);

        // 2️⃣ Render card into it
        Renderer<Card> renderer = CardRenderer.INSTANCE;
        renderer.render(card, animatedNode, from, true);

        // 3️⃣ Compute pixel positions
        double startX = from.getX() * TILE_SIZE;
        double startY = from.getY() * TILE_SIZE;

        //System.out.println("Start: " + startX + "," + startY);
        

        double endX = to.getX() * TILE_SIZE;
        double endY = to.getY() * TILE_SIZE;

        //System.out.println("End: " + endX + "," + endY);

        animatedNode.setTranslateX(startX);
        animatedNode.setTranslateY(startY);
        

        // 4️⃣ Add to floating layer
        floatingLayer.getChildren().add(animatedNode);

        // 5️⃣ Animate
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