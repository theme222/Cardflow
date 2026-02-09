package logic;

import component.mover.Conveyor;
import component.mover.Mover;
import placement.mover.ConveyorConstructor;
import util.*;

import java.util.HashMap;
import java.util.Objects;

public class PlayerInventory {
    // Player inventory needs to use GameLevel's methods and other stuff to place down the movers.
    // It doesn't handle the grid. Only the amount, the position, the rotation, and the removal.

    private static PlayerInventory instance;
    private HashMap<String, Integer> currentAvailableMovers; // This should be capital letters for everything.

    private Direction currentRotation;
    private String currentSelection;

    public HashMap<String, Integer> getCurrentAvailableMovers() { return currentAvailableMovers; }
    public void setCurrentAvailableMovers(HashMap<String, Integer> currentAvailableMovers) { this.currentAvailableMovers = currentAvailableMovers; }
    public Direction getCurrentRotation() { return currentRotation; }
    public void setCurrentRotation(Direction currentRotation) { this.currentRotation = currentRotation; }
    public void cycleRotation() { this.currentRotation = this.currentRotation.next(); }

    public static Mover getMoverObjectByName(String name, Direction rotation) {
        return switch (name) {
            case "CONVEYOR" -> new Conveyor(rotation);
            default -> throw new IllegalStateException("Unknown name: " + name);
        };
    }

    public void modifyAvailableMovers(String name, int change) {
        if (!currentAvailableMovers.containsKey(name)) throw new IllegalStateException("Unknown name: " + name);
        int resultValue = currentAvailableMovers.get(name);
        if (resultValue == -1) return; // Infinity
        resultValue += change;
        resultValue = Math.clamp(resultValue, 0, GameLevel.getInstance().AVAILABLE_MOVERS.get(name));
        currentAvailableMovers.put(name, resultValue);
    }

    public void setCurrentSelection(String name) {
        name = name.toUpperCase();
        if (!currentAvailableMovers.containsKey(name)) name = "";
        if (currentAvailableMovers.get(name) == 0) name = "";
        this.currentSelection = name;
    }

    public void placeToGrid(GridPos position) {
        // TODO: Maybe also do a check with the current game state?
        if (position == null) return;
        if (Objects.equals(currentSelection, "")) return;
        GameLevel game = GameLevel.getInstance();
        if (game.addMover(getMoverObjectByName(currentSelection, currentRotation), position)) {
            // Successfully added so we decrement the selection
            modifyAvailableMovers(currentSelection, -1);
        }
    }

    public void removeFromGrid(GridPos position) {
        // TODO: Maybe also do a check with the current game state?
        if (position == null) return;
        GameLevel game = GameLevel.getInstance();
        if (game.removeMover(game.getTile(position).getMover())) {
            // Successfully removed so we increment the selection
            modifyAvailableMovers(currentSelection, 1);
        }
    }

    public String getCurrentSelection() { return currentSelection; }
    public static PlayerInventory getInstance() { return instance; }
    public static void setInstance(PlayerInventory instance) { PlayerInventory.instance = instance; }

    public PlayerInventory() {
        // Ensure that GameLevel is initialized before this
        currentAvailableMovers = new HashMap<>(GameLevel.getInstance().AVAILABLE_MOVERS); // Copy total over
        if (currentAvailableMovers.isEmpty()) throw new IllegalStateException("No available movers");
        currentRotation = Direction.UP;
        currentSelection = "";
    }

}
