package testUtil;

import org.junit.jupiter.api.Test;
import util.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class TestDirection {
    @Test
    void testNextRotation() {
        assertEquals(Direction.RIGHT, Direction.UP.next());
        assertEquals(Direction.DOWN, Direction.RIGHT.next());
        assertEquals(Direction.LEFT, Direction.DOWN.next());
        assertEquals(Direction.UP, Direction.LEFT.next());
        assertEquals(Direction.STAY, Direction.STAY.next());
    }

    @Test
    void testPrevRotation() {
        assertEquals(Direction.LEFT, Direction.UP.prev());
        assertEquals(Direction.DOWN, Direction.LEFT.prev());
        assertEquals(Direction.RIGHT, Direction.DOWN.prev());
        assertEquals(Direction.UP, Direction.RIGHT.prev());
        assertEquals(Direction.STAY, Direction.STAY.prev());
    }

    @Test
    void testOpposite() {
        assertEquals(Direction.DOWN, Direction.UP.opposite());
        assertEquals(Direction.UP, Direction.DOWN.opposite());
        assertEquals(Direction.LEFT, Direction.RIGHT.opposite());
        assertEquals(Direction.RIGHT, Direction.LEFT.opposite());
        assertEquals(Direction.STAY, Direction.STAY.opposite());
    }

    @Test
    void testIsOpposite() {
        assertTrue(Direction.UP.isOpposite(Direction.DOWN));
        assertFalse(Direction.UP.isOpposite(Direction.LEFT));
        assertTrue(Direction.STAY.isOpposite(Direction.STAY));
    }

    @Test
    void testIsLeftOfAndIsRightOf() {
        assertTrue(Direction.UP.isLeftOf(Direction.RIGHT));
        assertFalse(Direction.UP.isLeftOf(Direction.LEFT));

        assertTrue(Direction.RIGHT.isRightOf(Direction.UP));
        assertFalse(Direction.RIGHT.isRightOf(Direction.DOWN));
    }

    @Test
    void testDeltas() {
        assertEquals(0, Direction.UP.dx());
        assertEquals(0, Direction.DOWN.dx());
        assertEquals(-1, Direction.LEFT.dx());
        assertEquals(1, Direction.RIGHT.dx());
        assertEquals(0, Direction.STAY.dx());

        assertEquals(-1, Direction.UP.dy());
        assertEquals(1, Direction.DOWN.dy());
        assertEquals(0, Direction.LEFT.dy());
        assertEquals(0, Direction.RIGHT.dy());
        assertEquals(0, Direction.STAY.dy());
    }

}