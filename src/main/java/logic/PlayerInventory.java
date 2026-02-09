package logic;

import component.mover.Mover;
import util.*;

import java.util.HashMap;

public class PlayerInventory {
    // Player inventory needs to use GameLevel's methods and other stuff to place down the movers.
    // It doesn't handle the grid. Only the amount, the position, the rotation, and the removal.

    private static PlayerInventory instance;
    private HashMap<String, Integer> currentAvailableMovers;

    private Direction currentRotation;
    private String currentSelection;

    public HashMap<String, Integer> getCurrentAvailableMovers() { return currentAvailableMovers; }
    public void setCurrentAvailableMovers(HashMap<String, Integer> currentAvailableMovers) { this.currentAvailableMovers = currentAvailableMovers; }
    public Direction getCurrentRotation() { return currentRotation; }
    public void setCurrentRotation(Direction currentRotation) { this.currentRotation = currentRotation; }
    public String getCurrentSelection() { return currentSelection; }
    public void setCurrentSelection(String currentSelection) { this.currentSelection = currentSelection; }
    public static PlayerInventory getInstance() { return instance; }
    public static void setInstance(PlayerInventory instance) { PlayerInventory.instance = instance; }

    public PlayerInventory() {
        // Ensure that GameLevel is initialized before this
        currentAvailableMovers = new HashMap<>(GameLevel.getInstance().AVAILABLE_MOVERS); // Copy total over
    }

}
