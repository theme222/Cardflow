package testComponent.mover;

import component.mover.FlipFlop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Direction;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestFlipFlop {
    private FlipFlop flipFlop;

    @BeforeEach
    void setUp() {
        flipFlop = new FlipFlop(Direction.RIGHT);
    }

    @Test
    void testInitialState() {
        assertTrue(flipFlop.isActive());
        assertEquals(Direction.RIGHT, flipFlop.getDirectionStateless());
    }

    @Test
    void testSetActive() {
        flipFlop.setActive(false);
        assertFalse(flipFlop.isActive());
        assertEquals(Direction.LEFT, flipFlop.getDirectionStateless());

        flipFlop.setActive(true);
        assertTrue(flipFlop.isActive());
        assertEquals(Direction.RIGHT, flipFlop.getDirectionStateless());
    }

    @Test
    void testValidOutputDirections() {
        Direction[] validOutputDirections = flipFlop.getValidOutputDirections();

        assertEquals(2, validOutputDirections.length);
        List<Direction> dirList = Arrays.asList(validOutputDirections);

        assertTrue(dirList.contains(Direction.RIGHT));
        assertTrue(dirList.contains(Direction.LEFT));
    }

    @Test
    void testReset() {
        flipFlop.setActive(false);
        assertFalse(flipFlop.isActive());
        assertEquals(Direction.LEFT, flipFlop.getDirectionStateless());

        flipFlop.reset();
        assertTrue(flipFlop.isActive());
        assertEquals(Direction.RIGHT, flipFlop.getDirectionStateless());
    }
}