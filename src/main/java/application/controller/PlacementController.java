package application.controller;

import application.controller.PlacementController.PlacementNode;
import application.path.PlacementPathfinder;
import application.view.GameView;
import component.mover.Mover;
import event.EventBus;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import logic.GameLevel;
import logic.PlayerInventory;
import logic.event.card.TileSelectChangeEvent;
import ui.levelinfo.LevelInfoPane;
import ui.overlay.SelectedTileOverlayRenderer;
import util.Direction;
import util.GridPos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

public class PlacementController {

    public static final PlacementController INSTANCE = new PlacementController();

    private String selectedTileName;
    private BiFunction<String, Direction, Mover> moverFactory;
    private Direction rotation;

    private GridPos dragStartPos;
    private GridPos currentMousePos;

    public static class PlacementNode {
        public GridPos pos;
        public Direction dir;
        public boolean delete;
    }

    private ArrayList<PlacementNode> placementList = new ArrayList<>();

    public void handleTileSelectChange(TileSelectChangeEvent event) {
        this.selectedTileName = event.getMovements();
        this.moverFactory = event.getFactory();
        this.rotation = event.getRotation();
        System.out.println(selectedTileName + moverFactory + rotation);
        SelectedTileOverlayRenderer.INSTANCE.setMoverDetails(moverFactory, selectedTileName);
    }

    public void handleOnMouseMove(GridPos pos) {
        //if (selectedTileName == null || moverFactory == null)
            //return; // No tile selected
        System.out.println("Mouse Move");
        currentMousePos = pos;
        dragStartPos = pos;
        updatePlacementList();
    }

    public void handleOnMouseExit(GridPos pos) {

        updatePlacementList();
        GameView.getInstance().updateTileAndAdjacent(pos);
    }

    public void handleMousePressed(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        //if (selectedTileName == null || moverFactory == null)
            //return; // No tile selected

        dragStartPos = gridPos;
        currentMousePos = gridPos;
        updatePlacementList();
    }

    public void handleRightClick() {
        rotation = rotation.next();
        updatePlacementList();
    }

    public void handleMouseDragged(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        //if (selectedTileName == null || moverFactory == null)
            //return; // No tile selected

        System.out.println("DRAG ");
        currentMousePos = gridPos;
        updatePlacementList();
    }

    public void handleMouseReleased(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        //if (selectedTileName == null || moverFactory == null)
            //return; // No tile selected

        for (PlacementNode node : placementList) {
            PlayerInventory.getInstance().setCurrentSelection(selectedTileName);
            PlayerInventory.getInstance().setCurrentRotation(node.dir);

            if (node.delete) {
                PlayerInventory.getInstance().removeFromGrid(node.pos);
            } else {
                PlayerInventory.getInstance().placeToGrid(node.pos);
            }

        }

        dragStartPos = currentMousePos;
        updatePlacementList();
        GameView.getInstance().getLevelInfoPane().updateInventoryUI();
    }

    private void updatePlacementList() {
        rebuildPlacementPath();
        System.out.println(placementList + " " + dragStartPos + " " + currentMousePos);
        SelectedTileOverlayRenderer.INSTANCE.updatePlacementList(placementList);
    }

    private void rebuildPlacementPath() {

        placementList.clear();

        if (dragStartPos == null || currentMousePos == null)
            return;

        GameLevel level = GameLevel.getInstance();

        boolean deleteMode = level.getTile(dragStartPos).getMover() != null;

        // --- COST FUNCTION ---
        PlacementPathfinder.CostFunction costFunction = (from, to, incomingDir, moveDir) -> {

            if (!level.isInBounds(to))
                return -1; // illegal

            boolean occupied = level.getTile(to).getMover() != null;

            // Delete mode: only allow deleting occupied tiles
            if (deleteMode) {
                if (!occupied)
                    return -1;
            } else {
                if (occupied)
                    return -1;
            }

            int cost = 1; // base movement

            // 180° block
            if (incomingDir != null &&
                    moveDir == incomingDir.opposite())
                return -1;

            // Turn penalty
            if (incomingDir != null &&
                    moveDir != incomingDir)
                cost += 2;

            return cost;
        };

        // --- HEURISTIC FUNCTION ---
        PlacementPathfinder.HeuristicFunction heuristic =
        (from, target, incomingDir, lastDir) -> {

            int dx = target.getX() - from.getX();
            int dy = target.getY() - from.getY();

            int manhattan = Math.abs(dx) + Math.abs(dy);

            // If still null (no facing info), fallback
            if (incomingDir == null) {
                return manhattan;
            }

            // --- Tactical bias ---
            boolean inFront = target.isPosInDirection(from, rotation);
            boolean behind  = target.isPosInDirection(from, rotation.opposite());

            int bias = 0;

            if (inFront) {
                // Prefer sideways first
                if (!rotation.isOpposite(incomingDir)) {
                    bias += manhattan;
                }
            } else if (behind) {
                // Prefer aligning
                if (!rotation.isPerpendicularOf(incomingDir)) {
                    bias += manhattan;
                }
            }

            return manhattan + bias;
        };

        var path = PlacementPathfinder.findPath(
                dragStartPos,
                currentMousePos,
                rotation,
                costFunction,
                heuristic);

        for (var p : path) {
            PlacementNode node = new PlacementNode();
            node.pos = p.pos;
            node.dir = p.dir;
            node.delete = deleteMode;
            placementList.add(node);
        }
    }

}
