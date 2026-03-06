package testComponent.mover;

import component.card.Card;
import component.card.Material;
import component.card.Suit;
import component.mover.ParityFilter;
import logic.GameLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Direction;
import util.GridPos;
import util.TestLevel;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestParityFilter {
    private ParityFilter filter;
    private GameLevel level;
    private GridPos pos;

    @BeforeEach
    void setUp() {
        level = TestLevel.initGlobalGameLevel();
        pos = new GridPos(2, 2);

        filter = new ParityFilter(Direction.UP);
        filter.setGridPos(pos);
        level.addMover(filter, pos);
    }

    @Test
    void testValidOutputDirections() {
        Direction[] validOutputDirections = filter.getValidOutputDirections();
        assertEquals(2, validOutputDirections.length);
        List<Direction> dirList = Arrays.asList(validOutputDirections);
        assertTrue(dirList.contains(Direction.UP));
        assertTrue(dirList.contains(Direction.LEFT));
    }

    @Test
    void testGetDirectionEvenCard() {
        Card card = new Card(Suit.HEART, 4, Material.PLASTIC);
        level.addCard(card, pos);
        assertEquals(Direction.UP, filter.getDirectionStateless());
    }

    @Test
    void testGetDirectionOddCard() {
        Card card = new Card(Suit.HEART, 3, Material.PLASTIC);
        level.addCard(card, pos);
        assertEquals(Direction.LEFT, filter.getDirectionStateless());
    }

    @Test
    void testGetDirectionNoCard() {
        assertEquals(Direction.STAY, filter.getDirectionStateless());
    }
}