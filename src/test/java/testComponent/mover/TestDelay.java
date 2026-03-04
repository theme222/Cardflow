package testComponent.mover;

import component.card.Card;
import component.card.Material;
import component.card.Suit;
import component.mover.Delay;
import logic.GameLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Direction;
import util.GridPos;
import util.TestLevel;

import static org.junit.jupiter.api.Assertions.*;

public class TestDelay {
    private Delay delay;
    private GameLevel level;
    private GridPos gridPos;

    @BeforeEach
    void setUp() {
        level = TestLevel.initGlobalGameLevel();
        gridPos = new GridPos(2, 2);

        delay = new Delay(Direction.DOWN);
        delay.setGridPos(gridPos);
        level.addMover(delay, gridPos);
    }

    @Test
    void testValidOutputDirections() {
        Direction[] validOutputDirections = delay.getValidOutputDirections();
        assertEquals(1, validOutputDirections.length);
        assertEquals(Direction.DOWN, validOutputDirections[0]);
    }

    @Test
    void testGetDirection() {
        Card card1 = new Card(Suit.CLUB, 5, Material.PLASTIC);
        level.addCard(card1, gridPos);
        assertEquals(Direction.STAY, delay.getDirection());
        assertEquals(Direction.DOWN, delay.getDirection());
        level.removeCard(card1);

        Card card2 = new Card(Suit.HEART, 2, Material.PLASTIC);
        level.addCard(card2, gridPos);
        assertEquals(Direction.STAY, delay.getDirection());
        assertEquals(Direction.DOWN, delay.getDirection());
    }
}