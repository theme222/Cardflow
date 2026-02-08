package logic.level;

import component.GridIndexable;
import component.GameTile;
import component.card.Card;
import component.modifier.Modifier;
import component.mover.Mover;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class GameLevel {
    private static GameLevel instance;

    public static final int MAX_WIDTH = 9;
    public static final int MAX_HEIGHT = 9;

    public final int width;
    public final int height;
    public final String levelName;

    public final List<Card> inputCards;
    public final List<Card> outputCards;

    // A Card must exist in the set and in the level tile at the same time.
    // We are doing redundancy to improve speed (RAM is cheap anyways)
    private GameTile[][] grid;
    public final HashSet<Card> cardSet;
    public final HashSet<Modifier> modifierSet;
    public final HashSet<Mover> moverSet;
    public final HashSet<Point> changedPoints; // Positions on grid that needs a UI update
    private boolean currentTick; // True = movement False = modify TODO: CHANGE THIS LOL

    public GameLevel(
            String levelName,
            int width,
            int height,
            List<Card> inputCards,
            List<Card> outputCards,
            GameTile[][] grid,
            HashSet<Modifier> modifierSet
    ) {
        this.levelName = levelName;
        this.width = width;
        this.height = height;
        this.inputCards = inputCards;
        this.outputCards = outputCards;

        this.grid = grid;

        this.modifierSet = modifierSet;
        this.cardSet = new HashSet<>();
        this.moverSet = new HashSet<>();
        this.changedPoints = new HashSet<>();
        this.currentTick = true;
    }

    public GameTile getTile(Point p) { // I know I'm gonna accidentally switch y and x one of these days
        if (p.x < 0 || p.x >= MAX_WIDTH || p.y < 0 || p.y >= MAX_HEIGHT)
            throw new IllegalArgumentException("Invalid position");
        return grid[p.y][p.x];
    }


    // Returns success / failure
    public boolean setPositionOnGrid(GridIndexable gridIndexable, Point newPoint) {
        // DOES NOT REMOVE OLD POSITION AND DOES NOT ADD TO SET
        return setPositionOnGrid(gridIndexable, newPoint, false);
    }

    public boolean setPositionOnGrid(GridIndexable gridIndexable, Point newPoint, boolean force) {
        if (!force && getTile(gridIndexable.getGridPos()).getSameClassOnTile(gridIndexable) != null) return false;
        getTile(newPoint).setSameClassOnTile(gridIndexable);
        gridIndexable.setGridPos(newPoint);
        return true;
    }

    public boolean addCard(Card card, Point newPoint) {
        // Do nothing if position is occupied or cardSet contains the card already
        if (cardSet.contains(card)) return false;
        if (getTile(card.getGridPos()).getCard() != null) return false;
        if (newPoint == null) return false;

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

            static final HashMap<Point, MoveIntent> byCurrent = new HashMap<>();
            static final HashMap<Point, ArrayList<MoveIntent>> byResult = new HashMap<>();

            enum IntentStatus {
                UNRESOLVED,
                MOVED,
                BLOCKED
            }

            Card card;
            Mover.Direction direction;
            IntentStatus status;

            public MoveIntent(Card c, Mover.Direction d) {
                card = c;
                direction = d;
                status = direction == Mover.Direction.STAY ? IntentStatus.BLOCKED :  IntentStatus.UNRESOLVED;
            }

            public Point getResultPos() { // Returns new point
                Point t = Mover.getTranslationFromDirection(direction);
                Point resultPos = new Point(card.getGridPos());
                resultPos.translate(t.x, t.y);
                return resultPos;
            }

            public Point getCurrentPos() { // Returns new point
                return new Point(card.getGridPos());
            }

            private static ArrayList<Point> getSurroundingPoints(Point resultPos) {
                // If somebody has a better way of doing this please fix thx
                ArrayList<Point> surroundPos = new ArrayList<>();

                Point topPoint = new Point(resultPos.x, resultPos.y - 1);
                Point leftPoint = new Point(resultPos.x - 1, resultPos.y);
                Point rightPoint = new Point(resultPos.x + 1, resultPos.y);
                Point bottomPoint = new Point(resultPos.x, resultPos.y + 1);

                surroundPos.add(topPoint);
                surroundPos.add(leftPoint);
                surroundPos.add(rightPoint);
                surroundPos.add(bottomPoint);
                return surroundPos;
            }

            public static void resolveIntent(List<MoveIntent> intentList, HashSet<Point> seen) { // linear recursive function (we hate recursion)
                // intentList is a variable that should contain intents that have the same result position.
                if (intentList.isEmpty() || intentList.size() > 4) throw new IndexOutOfBoundsException("No move intents found");

                // intentList should have [1,4] members (top left right bottom)

                // Check where the intent is trying to go
                Point resultPos = intentList.getFirst().getResultPos();

                // Get the priority intent (Top -> Left -> Right -> Bottom)
                MoveIntent priorityIntent = null;
                ArrayList<Point> surroundPos = getSurroundingPoints(resultPos);

                for (Point p: surroundPos) {
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
                if (resultPos.x < 0 || resultPos.x >= MAX_WIDTH || resultPos.y < 0 || resultPos.y >= MAX_HEIGHT) {
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
            }
        }

        // Clear static variables to remove previous run info
        MoveIntent.byCurrent.clear();
        MoveIntent.byResult.clear();

        for (Card card: cardSet) {
            // Get direction from the mover under the card
            Mover mover = getTile(card.getGridPos()).getMover();
            Mover.Direction direction = mover == null ? Mover.Direction.STAY : mover.getDirection();

            // Calculate and fill up the intent hashmap for later access
            MoveIntent intent = new MoveIntent(card, direction);

            MoveIntent.byCurrent.put(intent.getCurrentPos(), intent);

            if (!MoveIntent.byResult.containsKey(intent.getResultPos()))
                MoveIntent.byResult.put(intent.getResultPos(), new ArrayList<>());

            MoveIntent.byResult.get(intent.getResultPos()).add(intent);
        }

        // Loop through intents and resolve them
        for (MoveIntent intent: MoveIntent.byCurrent.values())
            if (intent.status == MoveIntent.IntentStatus.UNRESOLVED)
                MoveIntent.resolveIntent(MoveIntent.byResult.get(intent.getResultPos()), new HashSet<>());

        for (MoveIntent intent: MoveIntent.byCurrent.values()) { // Commit all intents
            if (intent.status == MoveIntent.IntentStatus.MOVED) {
                if (getTile(intent.getCurrentPos()).getCard() == intent.card) // Remove old position if not yet overriden
                    getTile(intent.getCurrentPos()).removeSameClassOnTile(intent.card);

                setPositionOnGrid(intent.card, intent.getResultPos(), true);
                changedPoints.add(intent.getCurrentPos());
                changedPoints.add(intent.getResultPos());
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
            changedPoints.add(modifier.getGridPos()); // ASSUMPTION: ALL MODIFIERS AFFECT ONLY THERE OWN SQUARE
            modifier.modify();
        }
        System.out.println("DONE MODIFY TICK"); // TODO: DEBUG
    }

    public void doTick() {
        if (currentTick) doMovementTick();
        else doModifyTick();
        currentTick = !currentTick;
    }

    // GETTERS & SETTERS //

    public static GameLevel getInstance() { return instance; }
    public static void setInstance(GameLevel instance) { GameLevel.instance = instance; }
    public GameTile[][] getGrid() { return grid; }
    public void setGrid(GameTile[][] grid) { this.grid = grid; }

    // GETTERS & SETTERS //

    @Override
    public String toString() {
        return  "Level: " + levelName
                + "\nwidth: "
                + width
                + "\nheight: "
                + height
                + "\ninputCards: "
                + inputCards
                + "\noutputCards: "
                + outputCards;
    }
}
