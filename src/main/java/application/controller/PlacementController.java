package application.controller;

import application.controller.PlacementPathBuilder.PlacementNode;
import application.path.PlacementPathfinder;
import application.view.GameView;
import audio.AudioManager;
import component.GameTile;
import component.mover.Mover;
import engine.GameState;
import engine.TickEngine;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import logic.GameLevel;
import logic.PlayerInventory;
import logic.event.card.TileSelectChangeEvent;
import ui.game.GameGrid;
import ui.overlay.SelectedTileOverlayRenderer;
import util.Direction;
import util.GridPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Singleton controller responsible for managing the logic behind placing, rotating, 
 * and dragging tiles/movers within the game grid.
 * <p>
 * It interprets mouse input events and translates them into inventory updates 
 * and visual placement previews.
 */
public class PlacementController {

    /** The singleton instance of the PlacementController. */
    public static final PlacementController INSTANCE = new PlacementController();

    private String selectedTileName;
    private BiFunction<String, Direction, Mover> moverFactory;
    private Direction rotation;

    private GridPos dragStartPos;
    private GridPos currentMousePos;

    private List<PlacementNode> placementList = new ArrayList<>();

    /**
     * Cycles the current placement rotation to the next clockwise direction.
     * Triggers a UI refresh for the inventory and updates the current placement preview.
     */
    public void rotateCurrentRotation() {
        rotation = rotation.next();
        updatePlacementList();
        GameView.getInstance().getLevelInfoPane().updateInventoryUI();
    }

    /** * Updates the controller state when a new tile type is selected from the UI.
     * * @param event The event containing the new tile's name, factory, and default rotation.
     */
    public void handleTileSelectChange(TileSelectChangeEvent event) {
        this.selectedTileName = event.getMovements();
        this.moverFactory = event.getFactory();
        this.rotation = event.getRotation();
        SelectedTileOverlayRenderer.INSTANCE.setMoverDetails(moverFactory, selectedTileName);
    }

    /** * Updates the current tracking position of the mouse on the grid.
     * Resets the drag starting point to the current position.
     * * @param pos The current grid coordinates of the mouse.
     */
    public void handleOnMouseMove(GridPos pos) {
        currentMousePos = pos;
        dragStartPos = pos;
        updatePlacementList();
    }

    /** * Cleans up the placement preview and refreshes the visual state of the 
     * tile and its neighbors when the mouse leaves a grid cell.
     * * @param pos The grid position being exited.
     */
    public void handleOnMouseExit(GridPos pos) {
        updatePlacementList();
        GameView.getInstance().updateTileAndAdjacent(pos);
    }

    /** * Handles the initial mouse press. 
     * <ul>
     * <li><b>Secondary Button:</b> Rotates an existing mover on the grid or the current selection.</li>
     * <li><b>Other Buttons:</b> Initializes the dragging logic for path placement.</li>
     * </ul>
     * * @param event   The JavaFX MouseEvent triggered.
     * @param gridPos The grid position where the press occurred.
     */
    public void handleMousePressed(MouseEvent event, GridPos gridPos) {
        if (event.getButton() == MouseButton.SECONDARY) {
            Mover mover = GameLevel.getInstance().getTile(gridPos).getMover();
            if (mover != null && TickEngine.getGameState() == GameState.PLACING) {
                mover.rotate();
            } else {
                rotateCurrentRotation();
            }
            GameView.getInstance().updateTileAndAdjacent(gridPos);
            AudioManager.playSoundEffect("mover-rotate");
        }
        else if (event.getButton() != MouseButton.PRIMARY) {
            dragStartPos = gridPos;
            currentMousePos = gridPos;
            updatePlacementList();
        }
    }

    /** * Updates the placement path as the user drags the mouse across the grid.
     * Only processes logic if the Primary Mouse Button is active.
     * * @param event   The drag event.
     * @param gridPos The current grid position under the cursor during the drag.
     */
    public void handleMouseDragged(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        currentMousePos = gridPos;
        updatePlacementList();
    }

    /** * Finalizes the placement or removal of tiles based on the generated {@code placementList}.
     * Plays corresponding sound effects based on success or failure of the action.
     * * @param event   The release event.
     * @param gridPos The grid position where the mouse was released.
     */
    public void handleMouseReleased(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        for (PlacementNode node : placementList) {
            PlayerInventory.getInstance().setCurrentSelection(selectedTileName);
            PlayerInventory.getInstance().setCurrentRotation(node.getDir());

            if (node.getDel()) {
                if (PlayerInventory.getInstance().removeFromGrid(node.getPos())) {
                    AudioManager.playSoundEffect("mover-pickup");
                } else {
                    AudioManager.playSoundEffect("game-error");
                }
            } else {
                if (PlayerInventory.getInstance().placeToGrid(node.getPos())) {
                    AudioManager.playSoundEffect("mover-place");
                } else {
                    AudioManager.playSoundEffect("game-error");
                }
            }
        }

        dragStartPos = currentMousePos;
        updatePlacementList();
        GameView.getInstance().getLevelInfoPane().updateInventoryUI();
    }

    /**
     * Recalculates the path of nodes between the drag start and current position.
     * Updates the visual overlay to show the user what will be placed.
     */
    private void updatePlacementList() {
        placementList = PlacementPathBuilder.buildPath(
                dragStartPos,
                currentMousePos,
                rotation);

        SelectedTileOverlayRenderer.INSTANCE.updatePlacementList(placementList);
    }

    /** * Gets the current rotation direction set for placement.
     * * @return The current {@link Direction}.
     */
    public Direction getRotation() {
        return rotation;
    }
}