package logic;

import audio.AudioManager;
import javafx.scene.effect.Effect;
import ui.effect.EffectManager;
import util.CardCount;
import util.GridIndexable;
import component.GameTile;
import component.card.Card;
import component.modifier.Modifier;
import component.mover.Mover;
import engine.TickEngine;
import event.EventBus;
import logic.movement.CardMovement;
import logic.movement.MovementTickResolver;
import util.GridPos;

import java.util.*;

import util.*;

/**
 * Represents a game level, including its dimensions, grid, components, and game state.
 * Manages the placement and interaction of cards, movers, and modifiers on the grid.
 */
public class GameLevel {
    /**
     * The singleton instance of the current GameLevel.
     */
    private static GameLevel instance;

    /**
     * The maximum allowed width for a level.
     */
    public static final int MAX_WIDTH = 9;
    /**
     * The maximum allowed height for a level.
     */
    public static final int MAX_HEIGHT = 9;

    // Constant throughout level //
    /**
     * The width of this level.
     */
    public final int WIDTH;
    /**
     * The height of this level.
     */
    public final int HEIGHT;
    /**
     * The display name of the level.
     */
    public final String LEVELNAME;
    /**
     * The unique identifier for the level.
     */
    public final String LEVELID;

    /**
     * The list of cards provided as input for this level.
     */
    public final List<CardCount> INPUT_CARDS;
    /**
     * The list of cards required for output to complete the level.
     */
    public final List<CardCount> OUTPUT_CARDS;

    /**
     * The initial counts of movers available for the player to place.
     */
    public final HashMap<String, Integer> AVAILABLE_MOVERS;
    // Constant throughout level //

    /**
     * The 2D array of tiles representing the game grid.
     */
    private GameTile[][] grid;
    /**
     * List of cards that have successfully exited the level.
     */
    public final ArrayList<Card> exitedCardsList;
    /**
     * Set of all active cards currently on the grid.
     */
    public final HashSet<Card> cardSet;
    /**
     * Set of all modifiers present in the level.
     */
    public final HashSet<Modifier> modifierSet;
    /**
     * Set of all movers currently placed on the grid.
     */
    public final HashSet<Mover> moverSet;
    /**
     * Set of grid positions that have changed during a tick and need UI updates.
     */
    public final HashSet<GridPos> changedPoints;
    /**
     * Set of modifiers that have successfully performed an action in the current tick.
     */
    public final HashSet<Modifier> successfullyModified;

    /**
     * Constructs a new GameLevel with the specified properties.
     * 
     * @param levelID Unique ID of the level.
     * @param levelName Name of the level.
     * @param width Width of the grid.
     * @param height Height of the grid.
     * @param inputCards Input card counts.
     * @param outputCards Target output card counts.
     * @param availableMovers Available movers for placement.
     * @param grid The initial grid setup.
     * @param modifierSet Set of modifiers initially on the grid.
     */
    public GameLevel(
            String levelID,
            String levelName,
            int width,
            int height,
            List<CardCount> inputCards,
            List<CardCount> outputCards,
            HashMap<String, Integer> availableMovers,
            GameTile[][] grid,
            HashSet<Modifier> modifierSet
    ) {
        this.LEVELID = levelID;
        this.LEVELNAME = levelName;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.INPUT_CARDS = inputCards;
        this.OUTPUT_CARDS = outputCards;
        this.AVAILABLE_MOVERS = availableMovers;

        this.grid = grid;

        this.modifierSet = modifierSet;
        this.cardSet = new HashSet<>(); // Could move this and the moverSet outside incase we do a load from "save state" or other things like that
        this.moverSet = new HashSet<>();
        this.exitedCardsList = new ArrayList<>();
        this.changedPoints = new HashSet<>();
        this.successfullyModified = new HashSet<>();
    }

    /** 
     * Retrieves the tile at a specific grid position.
     * 
     * @param p The {@link GridPos} to look up.
     * @return The {@link GameTile} at that position.
     * @throws IllegalArgumentException if the position is out of bounds.
     */
    public GameTile getTile(GridPos p) { // I know I'm gonna accidentally switch y and x one of these days
        if (!isInBounds(p))
            throw new IllegalArgumentException("Invalid position");
        return grid[p.getY()][p.getX()];
    }

    /** 
     * Checks if a grid position is within the level boundaries.
     * 
     * @param p The {@link GridPos} to check.
     * @return {@code true} if within bounds, {@code false} otherwise.
     */
    public boolean isInBounds(GridPos p){
        return p.inRange(0, WIDTH-1, 0, HEIGHT-1);
    }

    /** 
     * Gets the tiles adjacent to a given position.
     * 
     * @param p The {@link GridPos} to find neighbors for.
     * @return An array of 4 {@link GameTile} objects (some may be null).
     */
    public GameTile[] getAdjacentTiles(GridPos p) { // hehe more helpers for meeeeeeeee
        GameTile[] adjacent = new GameTile[4];
        int x = p.getX();
        int y = p.getY();
        if (x > 0) adjacent[0] = grid[y][x-1]; else adjacent[0] = null;
        if (x < WIDTH-1) adjacent[1] = grid[y][x+1]; else adjacent[1] = null;
        if (y > 0) adjacent[2] = grid[y-1][x]; else adjacent[2] = null;
        if (y < HEIGHT-1) adjacent[3] = grid[y+1][x]; else adjacent[3] = null;
        return adjacent;
    }


    /** 
     * Sets an indexable component's position on the grid.
     * 
     * @param gridIndexable The component to place.
     * @param newPoint The new {@link GridPos}.
     * @return {@code true} if successful.
     */
    public boolean setPositionOnGrid(GridIndexable gridIndexable, GridPos newPoint) {
        // DOES NOT REMOVE OLD POSITION AND DOES NOT ADD TO SET
        return setPositionOnGrid(gridIndexable, newPoint, false);
    }

    /** 
     * Sets an indexable component's position on the grid, optionally forcing the update.
     * 
     * @param gridIndexable The component to place.
     * @param newPoint The new {@link GridPos}.
     * @param force Whether to overwrite an existing component of the same type.
     * @return {@code true} if successful.
     */
    public boolean setPositionOnGrid(GridIndexable gridIndexable, GridPos newPoint, boolean force) {
        if (!force && getTile(newPoint).getSameClassOnTile(gridIndexable) != null) return false;
        getTile(newPoint).setSameClassOnTile(gridIndexable);
        gridIndexable.setGridPos(newPoint);
        return true;
    }

    /** 
     * Adds a mover to the grid at a specified position.
     * 
     * @param mover The {@link Mover} to add.
     * @param newPoint The {@link GridPos} where it should be placed.
     * @return {@code true} if addition was successful, {@code false} otherwise.
     */
    public boolean addMover(Mover mover, GridPos newPoint) {
        // Do nothing if position is occupied or cardSet contains the card already
        if (moverSet.contains(mover)) return false;
        if (newPoint == null) return false;
        if (getTile(newPoint).getMover() != null) return false;
        if (!setPositionOnGrid(mover, newPoint)) return false;

        moverSet.add(mover);
        return true;
    }

    /** 
     * Removes a mover from the grid.
     * 
     * @param mover The {@link Mover} to remove.
     * @return {@code true} if removal was successful.
     */
    public boolean removeMover(Mover mover) {
        // Do nothing if it can't find the old mover
        if (!moverSet.contains(mover)) return false;
        if (getTile(mover.getGridPos()).getMover() == null) return false;

        getTile(mover.getGridPos()).removeSameClassOnTile(mover);
        moverSet.remove(mover);
        return true;
    }

    /** 
     * Adds a card to the grid at a specified position.
     * 
     * @param card The {@link Card} to add.
     * @param newPoint The {@link GridPos} where it should be placed.
     * @return {@code true} if addition was successful.
     */
    public boolean addCard(Card card, GridPos newPoint) {
        // Do nothing if position is occupied or cardSet contains the card already
        if (cardSet.contains(card)) return false;
        if (newPoint == null) return false;
        if (getTile(newPoint).getCard() != null) return false;

        if (!setPositionOnGrid(card, newPoint)) return false;
        cardSet.add(card);
        return true;
    }

    /** 
     * Removes a card from the grid.
     * 
     * @param card The {@link Card} to remove.
     * @return {@code true} if removal was successful.
     */
    public boolean removeCard(Card card) {
        // Do nothing if it can't find the old card
        if (!cardSet.contains(card)) return false;
        if (getTile(card.getGridPos()).getCard() == null) return false;

        getTile(card.getGridPos()).removeSameClassOnTile(card);
        cardSet.remove(card);
        return true;
    }

    /**
     * Executes the movement phase of a game tick.
     * Resolves all card movements and emits an {@link AfterMovementEvent}.
     */
    public void doMovementTick() {
        HashSet<CardMovement> movements = MovementTickResolver.doMovementTick(this, cardSet, changedPoints); // this is what a lobotomy looks like :D
        EventBus.emit(new logic.event.AfterMovementEvent(movements)); // Let the world know we are done with movement tick so we can do modify tick and other things
        if (!movements.isEmpty()) AudioManager.playSoundEffect("card-move");
    }

    /**
     * Executes the modification phase of a game tick.
     * Invokes all modifiers on the grid and triggers associated effects and sounds.
     */
    public void doModifyTick() {
        changedPoints.clear();
        // Round two baby lets do this
        for (Modifier modifier: modifierSet) {
            changedPoints.add(modifier.getGridPos()); // ASSUMPTION: ALL MODIFIERS AFFECT ONLY THEIR OWN SQUARE
            modifier.modify(getTile(modifier.getGridPos()).getCard()); // on success each call should add their class to the hashset
        }
        AudioManager.playSFXWithModifierSet(successfullyModified);
        EffectManager.createEffectsWithModifierSet(successfullyModified);
        successfullyModified.clear();
    }

    /**
     * Resets the level to its initial state, removing all cards and resetting movers and modifiers.
     */
    public void resetLevel() {

        Card[] arr = cardSet.toArray(new Card[0]); // doing it like this because god knows whats gonna happen if I iterate through the actual set
        for (Card card: arr) {
            changedPoints.add(card.getGridPos());
            removeCard(card);
        }

        for (Mover mover: moverSet) {
            changedPoints.add(mover.getGridPos());
            mover.reset();
        }

        for (Modifier modifier: modifierSet) {
            changedPoints.add(modifier.getGridPos());
            modifier.reset();
        }

        exitedCardsList.clear();
    }

    /** 
     * Gets the singleton instance of {@link GameLevel}.
     * 
     * @return The active {@link GameLevel} instance.
     * @throws IllegalStateException if the instance has not been initialized.
     */
    public static GameLevel getInstance() {
        if (instance == null) throw new IllegalStateException("GameLevel has not been initialized");
        return instance;
    }

    /**
     * Sets the singleton instance of {@link GameLevel}.
     * 
     * @param instance The instance to set.
     */
    public static void setInstance(GameLevel instance) { GameLevel.instance = instance; }

    /**
     * Gets the 2D grid of tiles.
     * 
     * @return A 2D array of {@link GameTile}.
     */
    public GameTile[][] getGrid() { return grid; }

    /**
     * Sets the grid of tiles.
     * 
     * @param grid A 2D array of {@link GameTile}.
     */
    public void setGrid(GameTile[][] grid) { this.grid = grid; }

    /** 
     * Returns a string representation of the level's basic info.
     * 
     * @return A descriptive string.
     */
    @Override
    public String toString() {
        return  "Level: " + LEVELNAME
                + "\nwidth: "
                + WIDTH
                + "\nheight: "
                + HEIGHT
                + "\ninputCards: "
                + INPUT_CARDS
                + "\noutputCards: "
                + OUTPUT_CARDS;
    }
}
