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

/**
 * The {@code TooltipLayer} is a specialized UI pane that manages the 
 * display of dynamic, hierarchical tooltips.
 * <p>
 * It listens for mouse events on registered nodes and automatically 
 * positions and updates tooltip information based on the {@link Tippable} 
 * object associated with that node.
 */
public class TooltipLayer extends Pane {

    private final TooltipHContainer tooltipHContainer;
    private Tippable tooltipTarget;
    private final Runnable unregisterModifyEnd;
    private final Runnable unregisterMovementEnd;

    /**
     * Constructs a new {@code TooltipLayer} and sets up global event listeners 
     * to keep tooltips synchronized with game state changes.
     */
    public TooltipLayer() {
        tooltipHContainer = new TooltipHContainer();

        setMouseTransparent(true);
        setPickOnBounds(false);
        getChildren().add(tooltipHContainer);

        unregisterModifyEnd = EventBus.register(ModifyEndedEvent.class, this::updateTooltipInfo);
        unregisterMovementEnd = EventBus.register(MovementEndedEvent.class, this::updateTooltipInfo);
    }

    /**
     * Unregisters event listeners to prevent memory leaks when the layer is discarded.
     */
    public void cleanup() {
        unregisterModifyEnd.run();
        unregisterMovementEnd.run();
    }

    /** 
     * Binds a UI node to a {@link Tippable} source.
     * @param node The JavaFX node that triggers the tooltip on hover.
     * @param tooltipTarget The object providing the tooltip content.
     */
    public void bind(Node node, Tippable tooltipTarget) {
        node.addEventHandler(MouseEvent.MOUSE_ENTERED,(e) -> { showTooltip(e, tooltipTarget); });
        node.addEventHandler(MouseEvent.MOUSE_MOVED,this::setTooltipPosition);
        node.addEventHandler(MouseEvent.MOUSE_EXITED,this::hideTooltip);
    }

    /** 
     * Initiates the display of a tooltip with a fade-in animation.
     * @param e The mouse event that triggered the display.
     * @param tooltipTarget The content source.
     */
    private void showTooltip(MouseEvent e, Tippable tooltipTarget) {
        FadeTransition fade = new FadeTransition(Duration.millis(200), tooltipHContainer);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        this.tooltipTarget = tooltipTarget;

        setTooltipPosition(e);
        updateTooltipInfo(new Event() {});
    }

    /** 
     * Positions the tooltip container near the mouse cursor, adjusting 
     * for screen boundaries.
     * @param e The current mouse move event.
     */
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

    /** 
     * Refreshes the tooltip's visual content.
     * <p>
     * This method recursively resolves references within the tooltip tree 
     * to build a multi-column display of related information.
     * @param e The triggering event (can be a dummy event).
     */
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

    /** 
     * Hides the tooltip with a fade-out animation.
     * @param e The mouse exit event.
     */
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
    }
}

class TooltipVContainer extends VBox {
    public TooltipVContainer() {
        setAlignment(Pos.TOP_CENTER);
        setSpacing(15);
    }
}
