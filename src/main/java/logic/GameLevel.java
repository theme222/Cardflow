package logic;

import util.CardCount;
import util.GridIndexable;
import component.GameTile;
import component.card.Card;
import component.modifier.Modifier;
import component.mover.Mover;
import util.GridPos;

import java.util.*;
import java.util.List;

import util.*;


public class GameLevel {
    private static GameLevel instance;

    public static final int MAX_WIDTH = 9;
    public static final int MAX_HEIGHT = 9;

    // Constant throughout level //
    public final int WIDTH;
    public final int HEIGHT;
    public final String LEVELNAME;

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
    private boolean currentTick; // True = movement False = modify TODO: CHANGE THIS LOL (Also maybe move this to GameState)

    public GameLevel(
            String levelName,
            int width,
            int height,
            List<CardCount> inputCards,
            List<CardCount> outputCards,
            HashMap<String, Integer> availableMovers,
            GameTile[][] grid,
            HashSet<Modifier> modifierSet
    ) {
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
        this.currentTick = true;
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
        // If you are reading this I am so sorry on what you are about to witness
        // Also theoretically O(n) but honestly I have no idea if it actually is
        System.out.println("DOING MOVEMENT TICK"); // TODO: DEBUG

        changedPoints.clear();
        class MoveIntent {
            // TODO: DEBUG THIS FULLY AND MAKE IT BE COMPLIANT WITH GETTERS AND SETTERS

            static final HashMap<GridPos, MoveIntent> byCurrent = new HashMap<>();
            static final HashMap<GridPos, ArrayList<MoveIntent>> byResult = new HashMap<>();

            enum IntentStatus {
                UNRESOLVED,
                MOVED,
                BLOCKED
            }

            Card card;
            Direction direction;
            IntentStatus status;

            public MoveIntent(Card c, Direction d) {
                card = c;
                direction = d;
                status = direction == Direction.STAY ? IntentStatus.BLOCKED :  IntentStatus.UNRESOLVED;
            }

            public GridPos getResultPos() { // Returns new point
                GridPos t = Mover.getTranslationFromDirection(direction);
                GridPos resultPos = new GridPos(card.getGridPos());
                return resultPos.add(t);
            }

            @Override
            public String toString() {
                return "MoveIntent{" +
                        "card=" + card +
                        ", direction=" + direction +
                        ", status=" + status +
                        '}';
            }

            public GridPos getCurrentPos() { // Returns new point
                return new GridPos(card.getGridPos());
            }

            private static ArrayList<GridPos> getSurroundingPoints(GridPos resultPos) {
                // If somebody has a better way of doing this please fix thx
                ArrayList<GridPos> surroundPos = new ArrayList<>();

                GridPos topPoint = resultPos.add(0, -1);
                GridPos leftPoint = resultPos.add(-1, 0);
                GridPos rightPoint = resultPos.add(1, 0);
                GridPos bottomPoint = resultPos.add(0, 1);

                surroundPos.add(topPoint);
                surroundPos.add(leftPoint);
                surroundPos.add(rightPoint);
                surroundPos.add(bottomPoint);
                return surroundPos;
            }

            public static void resolveIntent(List<MoveIntent> intentList, HashSet<GridPos> seen) { // linear recursive function (we hate recursion)
                System.out.println("RESOLVING: " + intentList + " WITH SEEN: " + seen);
                // intentList is a variable that should contain intents that have the same result position.
                if (intentList.isEmpty() || intentList.size() > 4) throw new IndexOutOfBoundsException("No move intents found");

                // intentList should have [1,4] members (top left right bottom)

                // Check where the intent is trying to go
                GridPos resultPos = intentList.getFirst().getResultPos();

                // Get the priority intent (Top -> Left -> Right -> Bottom)
                MoveIntent priorityIntent = null;
                ArrayList<GridPos> surroundPos = getSurroundingPoints(resultPos);
                System.out.println(surroundPos);

                for (GridPos p: surroundPos) {
                    if (priorityIntent != null) break;
                    for (MoveIntent intent: intentList) {
                        if (intent.getCurrentPos().equals(p)) {
                            priorityIntent = intent;
                            break;
                        }
                    }
                }

                // Block all intents that aren't the priority
                for (MoveIntent intent: intentList) {
                    if (intent != priorityIntent)
                        intent.status = IntentStatus.BLOCKED;
                }

                if (priorityIntent == null) throw new RuntimeException("No move intents found ?????");

                // From here on we only care about the priority intent

                // Check within range
                if (!resultPos.inRange(0, GameLevel.getInstance().WIDTH-1, 0, GameLevel.getInstance().HEIGHT-1)) {
                    priorityIntent.status = IntentStatus.BLOCKED;
                    return;
                }

                // Check position contains Modifier / Mover that is blocking.
                Modifier resultPosMod = GameLevel.getInstance().getTile(resultPos).getModifier();
                Mover resultPosMove = GameLevel.getInstance().getTile(resultPos).getMover();

                if (
                    (resultPosMod != null && resultPosMod.isBlocking()) ||
                    (resultPosMove != null && resultPosMove.isBlocking())
                ) {
                    // Technically currently resultPosMove.isBlocking() will never return true but ya never know what might happen
                    priorityIntent.status = IntentStatus.BLOCKED;
                    return;
                }

                // Check position has a card or doesn't
                if (GameLevel.getInstance().getTile(resultPos).getCard() != null) {
                    // Resolve the next position
                    if (seen.contains(resultPos)) { // We have evaluated this position before (implies cycle)
                        // Checking whether cards are facing each other [C1 -> <- C2]
                        if (byCurrent.get(resultPos).getResultPos().equals(priorityIntent.getCurrentPos()))
                            priorityIntent.status = IntentStatus.BLOCKED;
                        else
                            priorityIntent.status = IntentStatus.MOVED;
                        return;
                    }

                    for (MoveIntent intent: intentList)  // Add all intents to seen
                        seen.add(intent.getCurrentPos());

                    MoveIntent blockingCardIntent = byCurrent.get(resultPos); // get MUST NOT ERROR OR BE NULL HERE

                    if (blockingCardIntent.status == IntentStatus.BLOCKED) { // If the position we are going to has a card that is blocked
                        priorityIntent.status = IntentStatus.BLOCKED;
                        return;
                    }
                    else if (blockingCardIntent.status == IntentStatus.MOVED) { // If the position we are going to has a card that has moved
                        priorityIntent.status = IntentStatus.MOVED;
                        return;
                    }

                    // the blocking card contains intent that is unresolved. we call resolve intent to collapse it
                    List<MoveIntent> resultIntentList = byResult.get(blockingCardIntent.getResultPos());
                    resolveIntent(resultIntentList, seen);

                    // Re check one more time lol
                    if (blockingCardIntent.status == IntentStatus.BLOCKED) { // If the position we are going to has a card that is blocked
                        priorityIntent.status = IntentStatus.BLOCKED;
                        return;
                    }
                    else if (blockingCardIntent.status == IntentStatus.MOVED) { // If the position we are going to has a card that has moved
                        priorityIntent.status = IntentStatus.MOVED;
                        return;
                    }
                    else throw new RuntimeException("What the actual hell happened here?");
                }
                else {
                    priorityIntent.status = IntentStatus.MOVED;
                    return;
                }
            }
        }

        // Clear static variables to remove previous run info
        MoveIntent.byCurrent.clear();
        MoveIntent.byResult.clear();

        for (Card card: cardSet) {
            // Get direction from the mover under the card
            Mover mover = getTile(card.getGridPos()).getMover();
            Direction direction = mover == null ? Direction.STAY : mover.getDirection();

            // Calculate and fill up the intent hashmap for later access
            MoveIntent intent = new MoveIntent(card, direction);

            MoveIntent.byCurrent.put(intent.getCurrentPos(), intent);

            if (!MoveIntent.byResult.containsKey(intent.getResultPos()))
                MoveIntent.byResult.put(intent.getResultPos(), new ArrayList<>());

            MoveIntent.byResult.get(intent.getResultPos()).add(intent);
        }

        System.out.println(MoveIntent.byCurrent);
        System.out.println(MoveIntent.byResult);

        // Loop through intents and resolve them
        for (MoveIntent intent: MoveIntent.byCurrent.values())
            if (intent.status == MoveIntent.IntentStatus.UNRESOLVED)
                MoveIntent.resolveIntent(MoveIntent.byResult.get(intent.getResultPos()), new HashSet<>());

        for (MoveIntent intent: MoveIntent.byCurrent.values()) { // Commit all intents
            if (intent.status == MoveIntent.IntentStatus.MOVED) {
                if (getTile(intent.getCurrentPos()).getCard() == intent.card) // Remove old position if not yet overriden
                    getTile(intent.getCurrentPos()).removeSameClassOnTile(intent.card);

                changedPoints.add(intent.getCurrentPos());
                changedPoints.add(intent.getResultPos());
                setPositionOnGrid(intent.card, intent.getResultPos(), true);
            }
            else if (intent.status == MoveIntent.IntentStatus.UNRESOLVED) {
                throw new RuntimeException("UHHHHHHHHHHHHHHHH HOW??????");
            }
        }
        System.out.println("DONE MOVEMENT TICK"); // TODO: DEBUG
    }

    public void doModifyTick() {
        changedPoints.clear();
        System.out.println("DOING MODIFY TICK"); // TODO: DEBUG
        // Round two baby lets do this
        for (Modifier modifier: modifierSet) {
            changedPoints.add(modifier.getGridPos()); // ASSUMPTION: ALL MODIFIERS AFFECT ONLY THEIR OWN SQUARE
            modifier.modify(getTile(modifier.getGridPos()).getCard());
        }
        System.out.println("DONE MODIFY TICK"); // TODO: DEBUG
    }

    public void doTick() {
        if (currentTick) doMovementTick();
        else doModifyTick();
        currentTick = !currentTick;
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
