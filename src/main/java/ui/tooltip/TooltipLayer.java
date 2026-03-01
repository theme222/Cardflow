package ui.tooltip;

import application.view.GameView;
import component.GameTile;
import engine.event.ModifyEndedEvent;
import engine.event.MovementEndedEvent;
import event.Event;
import event.EventBus;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import logic.GameLevel;
import util.GridPos;

import java.util.*;
import java.util.function.Supplier;

public class TooltipLayer extends Pane {

    // TOOLTIP WORKS LIKE THIS:
    // Tooltip Layer (Pane) contains one Tooltip HContainer
    // Tooltip HContainer (HBox) contains many Tooltip VContainer
    // Tooltip VContainer (VBox) contains many Tooltip Pane
    // Tooltip Pane (VBox) contains info on tooltip.title() and tooltip.description()
    // tooltip.description() is a textflow with styled text
    // Is this over the top complicated? Yes do I care? No.


    private final TooltipHContainer tooltipHContainer;
    private Tippable tooltipTarget;
    private final Runnable unregisterModifyEnd;
    private final Runnable unregisterMovementEnd;

    public TooltipLayer() {
        tooltipHContainer = new TooltipHContainer();

        setMouseTransparent(true);
        setPickOnBounds(false);
        getChildren().add(tooltipHContainer);

        unregisterModifyEnd = EventBus.register(ModifyEndedEvent.class, this::updateTooltipInfo);
        unregisterMovementEnd = EventBus.register(MovementEndedEvent.class, this::updateTooltipInfo);
    }

    public void cleanup() {
        unregisterModifyEnd.run();
        unregisterMovementEnd.run();
    }

    public void bind(Node node, Tippable tooltipTarget) {
        node.setOnMouseEntered((e) -> { showTooltip(e, tooltipTarget); });
        node.setOnMouseMoved(this::setTooltipPosition);
        node.setOnMouseExited(this::hideTooltip);
    }

    // Maybe use bind instead.
    private void showTooltip(MouseEvent e, Tippable tooltipTarget) {
        FadeTransition fade = new FadeTransition(Duration.millis(200), tooltipHContainer);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        this.tooltipTarget = tooltipTarget;

        setTooltipPosition(e);
        updateTooltipInfo(new Event() {});
    }

    private void setTooltipPosition(MouseEvent e) {
        double screenY = getHeight();
        double mouseY = e.getSceneY();

        boolean mouseIsTopHalfOfScreen = mouseY < screenY / 2;

        tooltipHContainer.setLayoutX(e.getSceneX() + 20);
        if (mouseIsTopHalfOfScreen) {
            tooltipHContainer.setLayoutY(e.getSceneY());
            tooltipHContainer.setAlignment(Pos.TOP_CENTER);
        }
        else {
            tooltipHContainer.setLayoutY(e.getSceneY() - tooltipHContainer.getHeight());
            tooltipHContainer.setAlignment(Pos.BOTTOM_CENTER);
        }

    }

    public void updateTooltipInfo(Event e) {

        List<List<Tooltip>> tooltipHInfo = new ArrayList<>();
        // We ignore the tooltip title of the first tip and take the list from desc
        if (tooltipTarget == null) return;
        Set<Tooltip> seenTips = new HashSet<>(); // prevents circular referencing loop
        List<Tooltip> tooltipVInfo = tooltipTarget.getTooltip().getRefs();

        do {
            tooltipHInfo.add(tooltipVInfo);
            List<Tooltip> nextTooltipContainerInfo = new ArrayList<>();

            for (Tooltip tooltip : tooltipVInfo) {

                // Functional programming in OOP
                nextTooltipContainerInfo.addAll(tooltip.getRefs().stream().filter(t -> {
                    if (seenTips.contains(t)) return false;
                    seenTips.add(t);
                    return true;
                }).toList());

            }

            tooltipVInfo = nextTooltipContainerInfo;
        } while (!tooltipVInfo.isEmpty()); // The rare instance where a do...while loop is actually used


        tooltipHContainer.getChildren().clear();
        for (List<Tooltip> curContainer: tooltipHInfo) {
            TooltipVContainer tooltipVContainer = new TooltipVContainer();
            for (Tooltip curTooltip: curContainer) {
                tooltipVContainer.getChildren().add(new TooltipPane(curTooltip));
            }
            tooltipHContainer.getChildren().add(tooltipVContainer);
        }
    }

    private void hideTooltip(MouseEvent e) {
        FadeTransition fade = new FadeTransition(Duration.millis(200), tooltipHContainer);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.play();
    }

}

class TooltipHContainer extends HBox {
    public TooltipHContainer() {
        setAlignment(Pos.TOP_LEFT); // Or bottom left
        setSpacing(10);
        setOpacity(0); // default invisible first
//        setBackground(Background.fill(Color.GREEN));
    }
}

class TooltipVContainer extends VBox {
    public TooltipVContainer() {
        setAlignment(Pos.TOP_CENTER);
        setSpacing(15);
    }
}
