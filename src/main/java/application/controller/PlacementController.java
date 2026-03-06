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

public class PlacementController {

    public static final PlacementController INSTANCE = new PlacementController();

    private String selectedTileName;
    private BiFunction<String, Direction, Mover> moverFactory;
    private Direction rotation;

    private GridPos dragStartPos;
    private GridPos currentMousePos;

    private List<PlacementNode> placementList = new ArrayList<>();

    public void rotateCurrentRotation() {
        rotation = rotation.next();
        updatePlacementList();
        GameView.getInstance().getLevelInfoPane().updateInventoryUI();
    }

    public void handleTileSelectChange(TileSelectChangeEvent event) {
        this.selectedTileName = event.getMovements();
        this.moverFactory = event.getFactory();
        this.rotation = event.getRotation();
        SelectedTileOverlayRenderer.INSTANCE.setMoverDetails(moverFactory, selectedTileName);
    }

    public void handleOnMouseMove(GridPos pos) {
        currentMousePos = pos;
        dragStartPos = pos;
        updatePlacementList();
    }

    public void handleOnMouseExit(GridPos pos) {

        updatePlacementList();
        GameView.getInstance().updateTileAndAdjacent(pos);
    }

    public void handleMousePressed(MouseEvent event, GridPos gridPos) {
        if (event.getButton() == MouseButton.SECONDARY) {
            Mover mover = GameLevel.getInstance().getTile(gridPos).getMover();
            if (mover != null && TickEngine.getGameState() == GameState.PLACING) mover.rotate();
            else rotateCurrentRotation();
            GameView.getInstance().updateTileAndAdjacent(gridPos);
            AudioManager.playSoundEffect("mover-rotate");
        }
        else if (event.getButton() != MouseButton.PRIMARY) {
            dragStartPos = gridPos;
            currentMousePos = gridPos;
            updatePlacementList();
        }
    }

    public void handleMouseDragged(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        currentMousePos = gridPos;
        updatePlacementList();
    }

    public void handleMouseReleased(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;

        for (PlacementNode node : placementList) {
            PlayerInventory.getInstance().setCurrentSelection(selectedTileName);
            PlayerInventory.getInstance().setCurrentRotation(node.getDir());

            if (node.getDel()) {
                if (PlayerInventory.getInstance().removeFromGrid(node.getPos())) AudioManager.playSoundEffect("mover-pickup");
                else AudioManager.playSoundEffect("game-error");
            } else {
                if (PlayerInventory.getInstance().placeToGrid(node.getPos())) AudioManager.playSoundEffect("mover-place");
                else AudioManager.playSoundEffect("game-error");
            }

        }

        dragStartPos = currentMousePos;
        updatePlacementList();
        GameView.getInstance().getLevelInfoPane().updateInventoryUI();
    }

    private void updatePlacementList() {
        placementList = PlacementPathBuilder.buildPath(
                dragStartPos,
                currentMousePos,
                rotation);

        SelectedTileOverlayRenderer.INSTANCE.updatePlacementList(placementList);
    }
    public Direction getRotation() {
        return rotation;
    }
}
