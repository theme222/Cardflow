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

public class PlayerInventory {
    // Player inventory needs to use GameLevel's methods and other stuff to place down the movers.
    // It doesn't handle the grid. Only the amount, the position, the rotation, and the removal.
    // Any and all user actions that involve modifying the grid must flow through the playerInventory class.

    private static PlayerInventory instance;
    private final GameLevel gameLevel;
    private final HashMap<String, Integer> currentAvailableMovers; // This should be capital letters for everything.

    private Direction currentRotation;
    private String currentSelection;

    public HashMap<String, Integer> getCurrentAvailableMovers() { return currentAvailableMovers; }
    public Direction getCurrentRotation() { return currentRotation; }
    public void setCurrentRotation(Direction currentRotation) { this.currentRotation = currentRotation; }
    
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

    public void modifyAvailableMovers(String name, int change) {
        if (!currentAvailableMovers.containsKey(name)) throw new IllegalStateException("Unknown name: " + name);
        int resultValue = currentAvailableMovers.get(name);
        if (resultValue == -1) return; // Infinity
        resultValue += change;
        resultValue = Math.clamp(resultValue, 0, gameLevel.AVAILABLE_MOVERS.get(name));
        currentAvailableMovers.put(name, resultValue);
    }

    public void setCurrentSelection(String name) {
        if (name != null) {
            name = name.toUpperCase();
            if (!currentAvailableMovers.containsKey(name)) name = null;
            if (currentAvailableMovers.get(name) == 0) name = null;
        }
        this.currentSelection = name;

        raiseSetEvent();
    }

    private void raiseSetEvent(){
        EventBus.emit(new TileSelectChangeEvent(this.currentSelection,this.currentRotation,PlayerInventory::getMoverObjectByName));
    }

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

    public String getCurrentSelection() { return currentSelection; }
    public static PlayerInventory getInstance() {
        if (instance == null) throw new IllegalStateException("PlayerInventory has not been initialized");
        return instance;
    }
    public static void setInstance(PlayerInventory instance) { PlayerInventory.instance = instance; }

    public PlayerInventory(GameLevel gameLevel) {
        System.out.println("PI Init");
        this.gameLevel = gameLevel;
        currentAvailableMovers = new HashMap<>(gameLevel.AVAILABLE_MOVERS); // Copy total over
        if (currentAvailableMovers.isEmpty()) throw new IllegalStateException("No available movers");
        currentRotation = Direction.UP;
        currentSelection = currentAvailableMovers.keySet().iterator().next(); // Just get the "first one" and put it as selection
        raiseSetEvent();
    }

}
