package testComponent.mover;

import component.mover.RedBlackFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Direction;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestRedBlackFilter {
    private RedBlackFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RedBlackFilter(Direction.RIGHT);
    }

    @Test
    void testValidOutputDirections() {
        Direction[] validOutputDirections = filter.getValidOutputDirections();

        assertEquals(2, validOutputDirections.length);

        List<Direction> dirList = Arrays.asList(validOutputDirections);

        assertTrue(dirList.contains(Direction.RIGHT));
        assertTrue(dirList.contains(Direction.UP));
    }

    // Cannot test getDirection() & getDirectionStateless() here
    // Filter logic should test in the Integration Test (TestGameLevel)
}