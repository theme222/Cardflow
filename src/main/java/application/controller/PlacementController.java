package application.controller;

import application.view.GameView;
import component.mover.Mover;
import event.EventBus;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import logic.GameLevel;
import logic.PlayerInventory;
import logic.event.card.TileSelectChangeEvent;
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

    private PlacementController() {
        // Subscribe to card selection changes
        EventBus.register(TileSelectChangeEvent.class, this::handleTileSelectChange);
    }

    private void handleTileSelectChange(TileSelectChangeEvent event) {
        this.selectedTileName = event.getMovements();
        this.moverFactory = event.getFactory();
        this.rotation = event.getRotation();
        SelectedTileOverlayRenderer.INSTANCE.setMoverDetails(moverFactory, selectedTileName);
    }

    public void handleOnMouseMove(GridPos pos) {
        if (selectedTileName == null || moverFactory == null)
            return; // No tile selected
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
        if (selectedTileName == null || moverFactory == null)
            return; // No tile selected

        dragStartPos = gridPos;
        currentMousePos = gridPos;
        updatePlacementList();
    }

    public void handleRightClick() {
        rotation = rotation.next();
    }

    public void handleMouseDragged(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (selectedTileName == null || moverFactory == null)
            return; // No tile selected

        System.out.println("DRAG ");
        currentMousePos = gridPos;
        updatePlacementList();
    }

    public void handleMouseReleased(MouseEvent event, GridPos gridPos) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (selectedTileName == null || moverFactory == null)
            return; // No tile selected

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
    }

    private void updatePlacementList() {
        rebuildPlacementPath();
        System.out.println(placementList + " " + dragStartPos + " " + currentMousePos);
        SelectedTileOverlayRenderer.INSTANCE.updatePlacementList(placementList);
    }

    private void rebuildPlacementPath() {

        placementList.clear();

        if (dragStartPos == null || currentMousePos == null) {
            return;
        }

        GameLevel level = GameLevel.getInstance();

        // ✅ Detect delete mode
        boolean deleteMode = level.getTile(dragStartPos).getMover() != null;

        record Node(GridPos pos, int cost) {
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        Map<GridPos, Integer> dist = new HashMap<>();
        Map<GridPos, GridPos> prev = new HashMap<>();

        pq.add(new Node(dragStartPos, 0));
        dist.put(dragStartPos, 0);

        while (!pq.isEmpty()) {
            Node cur = pq.poll();

            if (cur.pos.equals(currentMousePos))
                break;

            for (Direction dir : Direction.values()) {

                GridPos next = cur.pos.addDirection(dir);

                // OOB = dead end
                if (!level.isInBounds(next))
                    continue;

                int weight = 1;

                boolean occupied = level.getTile(next).getMover() != null;

                // Heavy weight if occupied
                if (deleteMode) {
                    // In delete mode, only allow walking over occupied tiles
                    if (!occupied)
                        continue;
                } else {
                    // In placement mode, discourage occupied tiles
                    if (occupied)
                        weight = 1000;
                }

                // 🔥 Perpendicular bias
                if (!deleteMode) {

                    if (dir == rotation) {
                        weight += 3; // discourage early forward growth
                    } else if (dir == rotation.opposite()) {
                        weight += 6; // strongly discourage going backwards
                    } else {
                        // perpendicular directions → cheapest
                        weight += 0;
                    }
                }

                int newCost = cur.cost + weight;

                if (!dist.containsKey(next) || newCost < dist.get(next)) {
                    dist.put(next, newCost);
                    prev.put(next, cur.pos);
                    pq.add(new Node(next, newCost));
                }
            }
        }

        // If unreachable, do nothing
        if (!prev.containsKey(currentMousePos) && !dragStartPos.equals(currentMousePos)) {
            return;
        }

        // Reconstruct path
        GridPos step = currentMousePos;

        List<GridPos> reversePath = new ArrayList<>();
        reversePath.add(step);

        while (!step.equals(dragStartPos)) {
            step = prev.get(step);
            reversePath.add(step);
        }

        Collections.reverse(reversePath);

        // Convert to PlacementNodes
        for (int i = 0; i < reversePath.size(); i++) {
            PlacementNode node = new PlacementNode();
            node.pos = reversePath.get(i);

            if (i == reversePath.size() - 1)
                node.dir = rotation;
            else {
                Direction dir = node.pos.directionTo(reversePath.get(i + 1));
                if (dir == null) {
                    node.dir = rotation;
                } else {
                    node.dir = dir;
                }
            }

            node.delete = deleteMode;
            placementList.add(node);
        }
        // System.out.println("=== PATH DEBUG ===");
        // System.out.println("Start: " + dragStartPos);
        // System.out.println("End: " + currentMousePos);
        // System.out.println("Prev contains end? " +
        // prev.containsKey(currentMousePos));
        // System.out.println("Path size: " + reversePath.size());
        // System.out.println("Path: " + reversePath);
        // System.out.println("==================");
        GameLevel.getInstance().getTile(dragStartPos).getMover();
    }

}
