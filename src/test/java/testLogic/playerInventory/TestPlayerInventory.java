package testLogic.playerInventory;

import component.mover.Conveyor;
import component.mover.Delay;
import component.mover.Mover;
import logic.GameLevel;
import logic.PlayerInventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Direction;
import util.GridPos;
import util.TestLevel;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayerInventory {
    private GameLevel level;
    private PlayerInventory inventory;

    @BeforeEach
    void setUp() {
        level = TestLevel.initGlobalGameLevel();

        level.AVAILABLE_MOVERS.put("CONVEYOR", 2);
        level.AVAILABLE_MOVERS.put("FLIPFLOP", 0);
        level.AVAILABLE_MOVERS.put("DELAY", -1);

        inventory = new PlayerInventory(level);
        PlayerInventory.setInstance(inventory);
    }

    @Test
    void testConstructorInitialState() {
        assertEquals(Direction.UP, inventory.getCurrentRotation());
        assertNotNull(inventory.getCurrentSelection());
    }

    @Test
    void testGetMoverObjectByName() {
        Mover mover1 = PlayerInventory.getMoverObjectByName("CONVEYOR", Direction.RIGHT);
        assertInstanceOf(Conveyor.class, mover1);
        assertEquals(Direction.RIGHT, mover1.getRotation());

        Mover mover2 = PlayerInventory.getMoverObjectByName("DELAY", Direction.DOWN);
        assertInstanceOf(Delay.class, mover2);
        assertEquals(Direction.DOWN, mover2.getRotation());

        assertThrows(IllegalStateException.class, () -> {
            PlayerInventory.getMoverObjectByName("UNKNOWN_MOVER", Direction.UP);
        });
    }

    @Test
    void testModifyAvailableMovers() {
        inventory.modifyAvailableMovers("CONVEYOR", -1);
        assertEquals(1, inventory.getCurrentAvailableMovers().get("CONVEYOR"));

        inventory.modifyAvailableMovers("CONVEYOR", 3);
        assertEquals(2, inventory.getCurrentAvailableMovers().get("CONVEYOR"));

        inventory.modifyAvailableMovers("DELAY", -2);
        assertEquals(-1, inventory.getCurrentAvailableMovers().get("DELAY"));
    }

    @Test
    void testSetCurrentSelection() {
        inventory.setCurrentSelection("CONVEYOR");
        assertEquals("CONVEYOR", inventory.getCurrentSelection());

        inventory.setCurrentSelection("FLIPFLOP");
        assertNull(inventory.getCurrentSelection());

        inventory.setCurrentSelection("PARITYFILTER");
        assertNull(inventory.getCurrentSelection());
    }

    @Test
    void testCycleRotation() {
        inventory.setCurrentRotation(Direction.UP);

        inventory.cycleRotation();
        assertEquals(Direction.RIGHT, inventory.getCurrentRotation());
        inventory.cycleRotation();
        assertEquals(Direction.DOWN, inventory.getCurrentRotation());
        inventory.cycleRotation();
        assertEquals(Direction.LEFT, inventory.getCurrentRotation());
        inventory.cycleRotation();
        assertEquals(Direction.UP, inventory.getCurrentRotation());
    }

    @Test
    void testPlaceAndRemoveFromGrid() {
        inventory.setCurrentSelection("CONVEYOR");
        GridPos gridPos = new GridPos(2, 2);

        assertTrue(inventory.placeToGrid(gridPos));
        assertNotNull(level.getTile(gridPos).getMover());
        assertEquals(1, inventory.getCurrentAvailableMovers().get("CONVEYOR"));

        assertTrue(inventory.removeFromGrid(gridPos));
        assertNull(level.getTile(gridPos).getMover());
        assertEquals(2, inventory.getCurrentAvailableMovers().get("CONVEYOR"));
    }

    @Test
    void testPlaceToGridEmptySelection() {
        inventory.modifyAvailableMovers("CONVEYOR", -2);
        inventory.setCurrentSelection("CONVEYOR");
        assertFalse(inventory.placeToGrid(new GridPos(1, 1)));
    }
}