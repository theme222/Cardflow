package logic;

import component.mover.*;
import engine.EngineState;
import engine.GameState;
import engine.TickEngine;
import event.EventBus;
import logic.event.card.TileSelectChangeEvent;
import util.*;

import java.util.HashMap;
import java.util.Objects;

/**
 * Manages the player's inventory of placeable components (Movers) and handles grid modifications.
 * This class serves as the primary interface for user actions related to placing or removing
 * movers on the game grid.
 */
public class PlayerInventory {
    /**
     * The singleton instance of PlayerInventory.
     */
    private static PlayerInventory instance;
    /**
     * The game level associated with this inventory.
     */
    private final GameLevel gameLevel;
    /**
     * A map storing the count of currently available movers by their uppercase names.
     */
    private final HashMap<String, Integer> currentAvailableMovers;

    /**
     * The current rotation to be applied to a mover when placed.
     */
    private Direction currentRotation;
    /**
     * The name of the currently selected mover type.
     */
    private String currentSelection;

    /**
     * Gets the map of currently available movers and their counts.
     * 
     * @return A {@link HashMap} of mover names to counts.
     */
    public HashMap<String, Integer> getCurrentAvailableMovers() { return currentAvailableMovers; }
    /**
     * Gets the current rotation setting.
     * 
     * @return The {@link Direction} of rotation.
     */
    public Direction getCurrentRotation() { return currentRotation; }
    /**
     * Sets the current rotation setting.
     * 
     * @param currentRotation The new {@link Direction} of rotation.
     */
    public void setCurrentRotation(Direction currentRotation) { this.currentRotation = currentRotation; }
    
    /** 
     * Creates a {@link Mover} object based on its name and rotation.
     * 
     * @param name The name of the mover type (e.g., "CONVEYOR").
     * @param rotation The initial {@link Direction} for the mover.
     * @return A new instance of the specified {@link Mover}.
     * @throws IllegalStateException if the name is unknown.
     */
    public static Mover getMoverObjectByName(String name, Direction rotation) {
        return switch (name) {
            case "CONVEYOR" -> new Conveyor(rotation);
            case "FLIPFLOP" -> new FlipFlop(rotation);
            case "PARITYFILTER" -> new ParityFilter(rotation);
            case "REDBLACKFILTER" -> new RedBlackFilter(rotation);
            case "DELAY" -> new Delay(rotation);
            default -> throw new IllegalStateException("Unknown name: " + name);
        };
    }

    /** 
     * Modifies the count of available movers in the inventory.
     * 
     * @param name The name of the mover type.
     * @param change The amount to change the count by (can be negative).
     * @throws IllegalStateException if the name is unknown.
     */
    public void modifyAvailableMovers(String name, int change) {
        if (!currentAvailableMovers.containsKey(name)) throw new IllegalStateException("Unknown name: " + name);
        int resultValue = currentAvailableMovers.get(name);
        if (resultValue == -1) return; // Infinity
        resultValue += change;
        resultValue = Math.clamp(resultValue, 0, gameLevel.AVAILABLE_MOVERS.get(name));
        currentAvailableMovers.put(name, resultValue);
    }

    /** 
     * Sets the currently selected mover type.
     * 
     * @param name The name of the mover to select, or {@code null} to deselect.
     */
    public void setCurrentSelection(String name) {
        if (name != null) {
            name = name.toUpperCase();
            // Fix NPE: Combined conditions to prevent unboxing null if the key doesn't exist
            if (!currentAvailableMovers.containsKey(name) || currentAvailableMovers.get(name) == 0) {
                name = null;
            }
        }
        this.currentSelection = name;

        raiseSetEvent();
    }

    /**
     * Emits a {@link TileSelectChangeEvent} to notify listeners of a selection or rotation change.
     */
    private void raiseSetEvent(){
        EventBus.emit(new TileSelectChangeEvent(this.currentSelection,this.currentRotation,PlayerInventory::getMoverObjectByName));
    }

    /** 
     * Attempts to place the currently selected mover onto the grid at the specified position.
     * 
     * @param position The {@link GridPos} where the mover should be placed.
     * @return {@code true} if placement was successful, {@code false} otherwise.
     */
    public boolean placeToGrid(GridPos position) { // returns success
        if (TickEngine.getGameState() == GameState.SIMULATING) return false;
        if (position == null) return false;
        if (Objects.isNull(currentSelection)) return false;
        if (currentAvailableMovers.get(currentSelection) == 0) return false;
        if (gameLevel.addMover(getMoverObjectByName(currentSelection, currentRotation), position)) {
            // Successfully added so we decrement the selection
            modifyAvailableMovers(currentSelection, -1);
            if (currentAvailableMovers.get(currentSelection) == 0) setCurrentSelection(null);
            return true;
        }
        return false;
    }

    /** 
     * Attempts to remove a mover from the grid at the specified position.
     * 
     * @param position The {@link GridPos} to remove the mover from.
     * @return {@code true} if removal was successful, {@code false} otherwise.
     */
    public boolean removeFromGrid(GridPos position) {
        if (TickEngine.getGameState() == GameState.SIMULATING) return false;
        if (position == null) return false;
        GameLevel game = GameLevel.getInstance();
        Mover toRemove = game.getTile(position).getMover();
        if (gameLevel.removeMover(gameLevel.getTile(position).getMover())) {
            // Successfully removed so we increment the selection
            modifyAvailableMovers(toRemove.getClass().getSimpleName().toUpperCase(), 1); // this is peak java idk what you are talking about
            return true;
        }
        return false;
    }

    /**
     * Gets the current selection name.
     * 
     * @return The selection name or {@code null}.
     */
    public String getCurrentSelection() { return currentSelection; }

    /** 
     * Gets the singleton instance of {@link PlayerInventory}.
     * 
     * @return The active instance.
     * @throws IllegalStateException if the instance has not been initialized.
     */
    public static PlayerInventory getInstance() {
        if (instance == null) throw new IllegalStateException("PlayerInventory has not been initialized");
        return instance;
    }

    /**
     * Sets the singleton instance of {@link PlayerInventory}.
     * 
     * @param instance The instance to set.
     */
    public static void setInstance(PlayerInventory instance) { PlayerInventory.instance = instance; }

    /**
     * Constructs a new PlayerInventory for the given game level.
     * 
     * @param gameLevel The {@link GameLevel} this inventory serves.
     * @throws IllegalStateException if there are no available movers in the level.
     */
    public PlayerInventory(GameLevel gameLevel) {
        this.gameLevel = gameLevel;
        currentAvailableMovers = new HashMap<>(gameLevel.AVAILABLE_MOVERS); // Copy total over
        if (currentAvailableMovers.isEmpty()) throw new IllegalStateException("No available movers");
        currentRotation = Direction.UP;
        currentSelection = currentAvailableMovers.keySet().iterator().next(); // Just get the "first one" and put it as selection
        raiseSetEvent();
    }

}
