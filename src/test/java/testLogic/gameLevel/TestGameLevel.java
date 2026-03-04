package testLogic.gameLevel;

import component.GameTile;
import component.card.Card;
import component.card.Material;
import component.card.Suit;
import component.modifier.changer.Adder;
import component.mover.Conveyor;
import component.mover.FlipFlop;
import logic.GameLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Direction;
import util.GridPos;
import util.TestLevel;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameLevel {
    private GameLevel level;

    @BeforeEach
    void setUp() {
        level = TestLevel.initGlobalGameLevel();
    }

    @Test
    void testAddAndRemoveCard() {
        Card card1 = new Card(Suit.HEART, 5, Material.PLASTIC);
        GridPos gridPos = new GridPos(2, 2);

        assertTrue(level.addCard(card1, gridPos));
        assertEquals(card1, level.getTile(gridPos).getCard());
        assertTrue(level.cardSet.contains(card1));

        Card card2 = new Card(Suit.SPADE, 3, Material.PLASTIC);
        assertFalse(level.addCard(card2, gridPos));

        assertTrue(level.removeCard(card1));
        assertNull(level.getTile(gridPos).getCard());
        assertFalse(level.cardSet.contains(card1));
    }

    @Test
    void testAddAndRemoveMover() {
        Conveyor conveyor = new Conveyor(Direction.UP);
        GridPos gridPos = new GridPos(1, 1);

        assertTrue(level.addMover(conveyor, gridPos));
        assertEquals(conveyor, level.getTile(gridPos).getMover());
        assertTrue(level.moverSet.contains(conveyor));

        assertTrue(level.removeMover(conveyor));
        assertNull(level.getTile(gridPos).getMover());
        assertFalse(level.moverSet.contains(conveyor));
    }

    @Test
    void testModifyTickIntegration() {
        Card card = new Card(Suit.HEART, 5, Material.PLASTIC);
        GridPos gridPos = new GridPos(2, 2);
        level.addCard(card, gridPos);

        Adder adder = new Adder(3);
        adder.setGridPos(gridPos);
        level.modifierSet.add(adder);

        level.doModifyTick();
        assertEquals(8, level.getTile(gridPos).getCard().getValue());
    }

    @Test
    void testMovementTickIntegration() {
        Card card = new Card(Suit.HEART, 5, Material.PLASTIC);
        GridPos gridPos1 = new GridPos(2, 2);
        GridPos gridPos2 = new GridPos(3, 2);
        level.addCard(card, gridPos1);

        Conveyor conveyor = new Conveyor(Direction.RIGHT);
        level.addMover(conveyor, gridPos1);

        level.doMovementTick();
        assertEquals(card, level.getTile(gridPos2).getCard());
        assertNull(level.getTile(gridPos1).getCard());
        assertEquals(gridPos2, card.getGridPos());
    }

    @Test
    void testResetLevel() {
        Card card = new Card(Suit.CLUB, 10, Material.STONE);
        level.addCard(card, new GridPos(0, 0));

        FlipFlop flipFlop = new FlipFlop(Direction.UP);
        level.addMover(flipFlop, new GridPos(1, 1));
        flipFlop.setActive(false);

        Adder adder = new Adder(5);
        adder.setGridPos(new GridPos(2, 2));
        level.modifierSet.add(adder);

        level.resetLevel();
        assertTrue(level.cardSet.isEmpty());
        assertNull(level.getTile(new GridPos(0, 0)).getCard());
        assertEquals(1, level.moverSet.size());
        assertTrue(flipFlop.isActive());
    }

    @Test
    void testGetTileOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            level.getTile(new GridPos(-1, 0));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            level.getTile(new GridPos(5, 5));
        });
    }

    @Test
    void testGetAdjacentTiles() {
        GameTile[] adjacent1 = level.getAdjacentTiles(new GridPos(2, 2));
        assertNotNull(adjacent1[0]); // Left
        assertNotNull(adjacent1[1]); // Right
        assertNotNull(adjacent1[2]); // Up
        assertNotNull(adjacent1[3]); // Down

        GameTile[] adjacent2 = level.getAdjacentTiles(new GridPos(0, 0));
        assertNull(adjacent2[0]);    // Left
        assertNotNull(adjacent2[1]); // Right
        assertNull(adjacent2[2]);    // Up
        assertNotNull(adjacent2[3]); // Down
    }

    @Test
    void testEdgeCasesForAddAndRemove() {
        Card card = new Card(Suit.HEART, 5, Material.PLASTIC);
        Conveyor conveyor = new Conveyor(Direction.UP);

        assertFalse(level.addCard(card, null));
        assertFalse(level.addMover(conveyor, null));
        assertFalse(level.removeCard(card));
        assertFalse(level.removeMover(conveyor));
    }

    @Test
    void testToString() {
        String levelString = level.toString();
        assertNotNull(levelString);
        assertTrue(levelString.contains("Level: test"));
        assertTrue(levelString.contains("width: 5"));
        assertTrue(levelString.contains("height: 5"));
    }

    @Test
    void testGetInstanceThrowsException() {
        GameLevel.setInstance(null);
        assertThrows(IllegalStateException.class, GameLevel::getInstance);
        GameLevel.setInstance(level);
    }

    @Test
    void testSetPositionOnGridForce() {
        Card card1 = new Card(Suit.HEART, 5, Material.PLASTIC);
        Card card2 = new Card(Suit.SPADE, 3, Material.STONE);
        GridPos gridPos = new GridPos(2, 2);

        level.setPositionOnGrid(card1, gridPos);
        assertEquals(card1, level.getTile(gridPos).getCard());
        assertFalse(level.setPositionOnGrid(card2, gridPos, false));
        assertEquals(card1, level.getTile(gridPos).getCard());
        assertTrue(level.setPositionOnGrid(card2, gridPos, true));
        assertEquals(card2, level.getTile(gridPos).getCard());
    }

    @Test
    void testGetAndSetGrid() {
        GameTile[][] grid1 = level.getGrid();
        assertNotNull(grid1);
        GameTile[][] grid2 = new GameTile[1][1];
        grid2[0][0] = new GameTile(null, 0, 0);
        level.setGrid(grid2);
        assertEquals(grid2, level.getGrid());
        level.setGrid(grid1);
    }
}