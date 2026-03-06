package testComponent.mover;

import component.card.Card;
import component.card.Material;
import component.card.Suit;
import component.mover.RedBlackFilter;
import logic.GameLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Direction;
import util.GridPos;
import util.TestLevel;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestRedBlackFilter {
    private RedBlackFilter filter;
    private GameLevel level;
    private GridPos gridPos;

    @BeforeEach
    void setUp() {
        level = TestLevel.initGlobalGameLevel();
        gridPos = new GridPos(2, 2);

        filter = new RedBlackFilter(Direction.RIGHT);
        filter.setGridPos(gridPos);
        level.addMover(filter, gridPos);
    }

    @Test
    void testValidOutputDirections() {
        Direction[] validOutputDirections = filter.getValidOutputDirections();
        assertEquals(2, validOutputDirections.length);
        List<Direction> dirList = Arrays.asList(validOutputDirections);
        assertTrue(dirList.contains(Direction.RIGHT));
        assertTrue(dirList.contains(Direction.UP));
    }

    @Test
    void testGetDirectionRedCard() {
        Card card1 = new Card(Suit.HEART, 5, Material.PLASTIC);
        level.addCard(card1, gridPos);
        assertEquals(Direction.RIGHT, filter.getDirectionStateless());
        level.removeCard(card1);
        Card card2 = new Card(Suit.DIAMOND, 5, Material.PLASTIC);
        level.addCard(card2, gridPos);
        assertEquals(Direction.RIGHT, filter.getDirectionStateless());
    }

    @Test
    void testGetDirectionBlackCard() {
        Card card1 = new Card(Suit.SPADE, 5, Material.PLASTIC);
        level.addCard(card1, gridPos);
        assertEquals(Direction.UP, filter.getDirectionStateless());
        level.removeCard(card1);
        Card card2 = new Card(Suit.CLUB, 5, Material.PLASTIC);
        level.addCard(card2, gridPos);
        assertEquals(Direction.UP, filter.getDirectionStateless());
    }

    @Test
    void testGetDirectionNoCard() {
        assertEquals(Direction.STAY, filter.getDirectionStateless());
    }
}