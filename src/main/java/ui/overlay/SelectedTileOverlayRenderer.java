package ui.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import application.controller.PlacementController;
import application.controller.PlacementPathBuilder.PlacementNode;
import application.view.GameView;
import component.GameTile;
import component.mover.Mover;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import logic.GameLevel;
import logic.event.card.TileSelectChangeEvent;
import registry.render.RendererRegistry;
import ui.render.Renderer;
import util.Direction;
import util.GridPos;

/**
 * The {@code SelectedTileOverlayRenderer} is a singleton responsible for 
 * rendering "ghost" or "preview" versions of tiles being placed by the user.
 * <p>
 * It interacts with the {@link PlacementController} to track the current 
 * mouse-dragged placement path and triggers UI updates only for affected tiles.
 */
public class SelectedTileOverlayRenderer {

    /** The singleton instance of the renderer. */
    public static final SelectedTileOverlayRenderer INSTANCE = new SelectedTileOverlayRenderer();

    // Selected tile data
    private BiFunction<String, Direction, Mover> moverFactory;
    private String moverName;

    private ArrayList<PlacementNode> placementListArrayList = new ArrayList<>();
    private ArrayList<PlacementNode> previousPlacementList = new ArrayList<>();

    // Ghost node (reuse instead of recreating)

    private SelectedTileOverlayRenderer() {
    }

    /** 
     * Configures the factory and name for the mover currently being placed.
     * @param moverFactory A function that creates a {@link Mover} given a name and direction.
     * @param moverName The internal ID of the mover type.
     */
    public void setMoverDetails(BiFunction<String, Direction, Mover> moverFactory, String moverName) {
        this.moverFactory = moverFactory;
        this.moverName = moverName;
    }

    /** 
     * Updates the internal list of nodes currently scheduled for placement.
     * <p>
     * This method calculates the difference between the old and new lists 
     * and forces a redraw on any grid positions that have changed.
     * @param newList The new list of {@link PlacementNode} objects.
     */
    public void updatePlacementList(List<PlacementNode> newList) {

        // Save old reference
        previousPlacementList = new ArrayList<>(placementListArrayList);

        // Replace current list
        placementListArrayList = new ArrayList<>(newList);

        runDiffAndUpdate();
    }

    /** 
     * Performs a diff between the previous and current placement paths 
     * and triggers visual updates on the {@link GameView}.
     */
    private void runDiffAndUpdate() {

        if (previousPlacementList == null)
            previousPlacementList = new ArrayList<>();

        // Build state maps keyed by position
        Map<GridPos, PlacementNode> oldMap = previousPlacementList.stream()
                .collect(java.util.stream.Collectors.toMap(
                        n -> n.getPos(),
                        n -> n));

        Map<GridPos, PlacementNode> newMap = placementListArrayList.stream()
                .collect(java.util.stream.Collectors.toMap(
                        n -> n.getPos(),
                        n -> n));

        // Union of all affected positions
        java.util.Set<GridPos> allPositions = new java.util.HashSet<>();
        allPositions.addAll(oldMap.keySet());
        allPositions.addAll(newMap.keySet());

        for (GridPos pos : allPositions) {

            PlacementNode oldNode = oldMap.get(pos);
            PlacementNode newNode = newMap.get(pos);

            boolean changed = false;

            if (oldNode == null || newNode == null) {
                // Appeared or disappeared
                changed = true;
            } else {
                // Same position — check rotation or delete mode
                if (oldNode.getDir() != newNode.getDir() ||
                        oldNode.getDel() != newNode.getDel()) {
                    changed = true;
                }
            }

            if (changed) {
                GameView.getInstance().updateTileAndAdjacent(pos);
            }
        }
    }

    /** 
     * Renders the ghost/preview tile onto the provided pane.
     * <p>
     * If the current position is marked for deletion in the placement path, 
     * a {@link DeleteOverlay} is shown instead of a mover preview.
     * @param overlayPane The pane where the preview should be rendered.
     * @param pos The grid position to render the preview for.
     */
    public void render(Pane overlayPane, GridPos pos) {

        for (PlacementNode node : placementListArrayList) {
            if (!node.getPos().equals(pos))
                continue;

            if (node.getDel()) {
                DeleteOverlay.INSTANCE.render(overlayPane);
                break;
            }

            if (moverFactory == null || moverName == null || node.getDir() == null) {
                continue; // nothing selected
            }

            Mover mover = moverFactory.apply(moverName, node.getDir());
            Renderer<Mover> renderer = RendererRegistry.INSTANCE.getRenderer(mover);
            renderer.render(mover, overlayPane, pos);
            overlayPane.setOpacity(0.5); // 50% transparent
            break;
        }
    }
}