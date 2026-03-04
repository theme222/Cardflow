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


public class GameLevel {
    private static GameLevel instance;

    public static final int MAX_WIDTH = 9;
    public static final int MAX_HEIGHT = 9;

    // Constant throughout level //
    public final int WIDTH;
    public final int HEIGHT;
    public final String LEVELNAME;
    public final String LEVELID;

    public final List<CardCount> INPUT_CARDS;
    public final List<CardCount> OUTPUT_CARDS;

    public final HashMap<String, Integer> AVAILABLE_MOVERS;
    // Constant throughout level //

    // A Card must exist in the set and in the level tile at the same time.
    // We are doing redundancy to improve speed (RAM is cheap anyways)
    private GameTile[][] grid;
    public final ArrayList<Card> exitedCardsList;
    public final HashSet<Card> cardSet;
    public final HashSet<Modifier> modifierSet;
    public final HashSet<Mover> moverSet;
    public final HashSet<GridPos> changedPoints; // Positions on grid that needs a UI update
    public final HashSet<Modifier> successfullyModified; // List of modifiers that successfully did their modification

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

    public GameTile getTile(GridPos p) { // I know I'm gonna accidentally switch y and x one of these days
        if (!p.inRange(0, WIDTH-1, 0, HEIGHT-1))
            throw new IllegalArgumentException("Invalid position");
        return grid[p.getY()][p.getX()];
    }

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


    // Returns success / failure
    public boolean setPositionOnGrid(GridIndexable gridIndexable, GridPos newPoint) {
        // DOES NOT REMOVE OLD POSITION AND DOES NOT ADD TO SET
        return setPositionOnGrid(gridIndexable, newPoint, false);
    }

    public boolean setPositionOnGrid(GridIndexable gridIndexable, GridPos newPoint, boolean force) {
        if (!force && getTile(newPoint).getSameClassOnTile(gridIndexable) != null) return false;
        getTile(newPoint).setSameClassOnTile(gridIndexable);
        gridIndexable.setGridPos(newPoint);
        return true;
    }

    public boolean addMover(Mover mover, GridPos newPoint) {
        // Do nothing if position is occupied or cardSet contains the card already
        if (moverSet.contains(mover)) return false;
        if (newPoint == null) return false;
        if (getTile(newPoint).getMover() != null) return false;
        if (!setPositionOnGrid(mover, newPoint)) return false;

        moverSet.add(mover);
        return true;
    }

    public boolean removeMover(Mover mover) {
        // Do nothing if it can't find the old mover
        if (!moverSet.contains(mover)) return false;
        if (getTile(mover.getGridPos()).getMover() == null) return false;

        getTile(mover.getGridPos()).removeSameClassOnTile(mover);
        moverSet.remove(mover);
        return true;
    }

    public boolean addCard(Card card, GridPos newPoint) {
        // Do nothing if position is occupied or cardSet contains the card already
        if (cardSet.contains(card)) return false;
        if (newPoint == null) return false;
        if (getTile(newPoint).getCard() != null) return false;

        if (!setPositionOnGrid(card, newPoint)) return false;
        cardSet.add(card);
        return true;
    }

    public boolean removeCard(Card card) {
        // Do nothing if it can't find the old card
        if (!cardSet.contains(card)) return false;
        if (getTile(card.getGridPos()).getCard() == null) return false;

        getTile(card.getGridPos()).removeSameClassOnTile(card);
        cardSet.remove(card);
        return true;
    }

    public void doMovementTick() {
        HashSet<CardMovement> movements = MovementTickResolver.doMovementTick(this, cardSet, changedPoints); // this is what a lobotomy looks like :D
        EventBus.emit(new logic.event.AfterMovementEvent(movements)); // Let the world know we are done with movement tick so we can do modify tick and other things
        if (!movements.isEmpty()) AudioManager.playSoundEffect("card-move");
    }

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

    // GETTERS & SETTERS //

    public static GameLevel getInstance() {
        if (instance == null) throw new IllegalStateException("GameLevel has not been initialized");
        return instance;
    }
    public static void setInstance(GameLevel instance) { GameLevel.instance = instance; }
    public GameTile[][] getGrid() { return grid; }
    public void setGrid(GameTile[][] grid) { this.grid = grid; }

    // GETTERS & SETTERS //

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
