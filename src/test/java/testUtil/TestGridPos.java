package testUtil;

import org.junit.jupiter.api.Test;
import util.Direction;
import util.GridPos;

import static org.junit.jupiter.api.Assertions.*;

public class TestGridPos {
    @Test
    void testConstructors() {
        GridPos defaultPos = new GridPos();
        assertEquals(0, defaultPos.getX());
        assertEquals(0, defaultPos.getY());

        GridPos pos = new GridPos(5, 10);
        assertEquals(5, pos.getX());
        assertEquals(10, pos.getY());

        GridPos copyPos = new GridPos(pos);
        assertEquals(5, copyPos.getX());
        assertEquals(10, copyPos.getY());

        GridPos nullCopy = new GridPos(null);
        assertEquals(0, nullCopy.getX());
        assertEquals(0, nullCopy.getY());
    }

    @Test
    void testSettersGetter() {
        GridPos pos = new GridPos();
        pos.setX(-3);
        pos.setY(7);
        assertEquals(-3, pos.getX());
        assertEquals(7, pos.getY());
    }

    @Test
    void testAddGridPos() {
        GridPos pos1 = new GridPos(2, 3);
        GridPos pos2 = new GridPos(4, 5);
        GridPos Pos3 = pos1.add(pos2);
        assertEquals(6, Pos3.getX());
        assertEquals(8, Pos3.getY());
        assertEquals(2, pos1.getX());

        GridPos posNull = pos1.add((GridPos)null);
        assertEquals(2, posNull.getX());
        assertEquals(3, posNull.getY());
    }

    @Test
    void testAddInts() {
        GridPos pos1 = new GridPos(5, -5);
        GridPos pos2 = pos1.add(-2, 3);

        assertEquals(3, pos2.getX());
        assertEquals(-2, pos2.getY());
    }

    @Test
    void testAddDirection() {
        GridPos pos = new GridPos(5, 5);

        GridPos upPos = pos.addDirection(Direction.UP);
        assertEquals(5, upPos.getX());
        assertEquals(4, upPos.getY());

        GridPos downPos = pos.addDirection(Direction.DOWN);
        assertEquals(5, downPos.getX());
        assertEquals(6, downPos.getY());

        GridPos rightPos = pos.addDirection(Direction.RIGHT);
        assertEquals(6, rightPos.getX());
        assertEquals(5, rightPos.getY());

        GridPos leftPos = pos.addDirection(Direction.LEFT);
        assertEquals(4, leftPos.getX());
        assertEquals(5, leftPos.getY());
    }

    @Test
    void testInRange() {
        GridPos pos = new GridPos(3, 4);
        assertTrue(pos.inRange(0, 8, 0, 8));
        assertTrue(pos.inRange(3, 8, 4, 8));
        assertFalse(pos.inRange(4, 8, 0, 8));
        assertFalse(pos.inRange(0, 2, 0, 8));
        assertFalse(pos.inRange(0, 8, 5, 8));
        assertFalse(pos.inRange(0, 8, 0, 3));
    }

    @Test
    void testEqualsAndHashCode() {
        GridPos pos1 = new GridPos(2, 2);
        GridPos pos2 = new GridPos(2, 2);
        GridPos pos3 = new GridPos(3, 2);

        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
        assertNotEquals(null, pos1);
        assertNotEquals(new Object(), pos1);
        assertEquals(pos1.hashCode(), pos2.hashCode());
        assertNotEquals(pos1.hashCode(), pos3.hashCode());
    }
}