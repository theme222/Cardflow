package testComponent.mover;

import component.mover.Delay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class TestDelay {
    private Delay delay;

    @BeforeEach
    void setUp() {
        delay = new Delay(Direction.DOWN);
    }

    @Test
    void testValidOutputDirections() {
        Direction[] validOutputDirections = delay.getValidOutputDirections();

        assertEquals(1, validOutputDirections.length);
        assertEquals(Direction.DOWN, validOutputDirections[0]);
    }

    // Cannot test getDirection() & getDirectionStateless() here
    // Delay Logic should test in Integration Test (TestGameLevel)
}