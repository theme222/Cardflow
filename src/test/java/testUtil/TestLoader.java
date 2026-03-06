package testUtil;

import static org.junit.jupiter.api.Assertions.*;

import component.card.Card;
import component.card.Material;
import component.card.Suit;
import component.modifier.Modifier;
import component.modifier.changer.Adder;
import component.modifier.changer.SuitSetter;
import component.modifier.combinator.Merger;
import logic.GameLevel;
import util.LevelLoader;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestLoader {
    @Test
    void testLoadValidLevel() {
        assertDoesNotThrow(() -> {
            GameLevel level = LevelLoader.loadLevel("1");
            GameLevel.setInstance(level);
            assertNotNull(level);
            assertTrue(level.WIDTH > 0);
            assertTrue(level.HEIGHT > 0);
        });
    }

    @Test
    void testLoadSandboxLevel() {
        assertDoesNotThrow(() -> {
            GameLevel sandbox = LevelLoader.loadLevel("sandbox");
            assertNotNull(sandbox);
            assertEquals("SANDBOX", sandbox.LEVELNAME);
        });
    }

    @Test
    void testLoadLevelOutOfBounds() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> LevelLoader.loadLevel("0"));
        assertThrows(IllegalArgumentException.class, () -> LevelLoader.loadLevel("-1"));
        assertThrows(IllegalArgumentException.class, () -> LevelLoader.loadLevel(String.valueOf(LevelLoader.TOTAL_LEVELS + 1)));
    }

    /** 
     * @throws Exception
     */
    @Test
    void testParseSuit() throws Exception {
        Method parseSuit = LevelLoader.class.getDeclaredMethod("parseSuit", String.class);
        parseSuit.setAccessible(true);

        assertEquals(Suit.HEART, parseSuit.invoke(null, "HEART"));
        assertEquals(Suit.HEART, parseSuit.invoke(null, "h"));
        assertEquals(Suit.SPADE, parseSuit.invoke(null, "S"));
        assertEquals(Suit.CLUB, parseSuit.invoke(null, "CLUB"));
        assertEquals(Suit.DIAMOND, parseSuit.invoke(null, "d"));

        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> parseSuit.invoke(null, "INVALID"));
        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
    }

    /** 
     * @throws Exception
     */
    @Test
    void testParseMaterial() throws Exception {
        Method parseMaterial = LevelLoader.class.getDeclaredMethod("parseMaterial", String.class);
        parseMaterial.setAccessible(true);

        assertEquals(Material.PLASTIC, parseMaterial.invoke(null, "PLASTIC"));
        assertEquals(Material.PLASTIC, parseMaterial.invoke(null, "p"));
        assertEquals(Material.GLASS, parseMaterial.invoke(null, "G"));
        assertEquals(Material.CORRUPTED, parseMaterial.invoke(null, "c"));

        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> parseMaterial.invoke(null, "WOOD"));
        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
    }

    /** 
     * @throws Exception
     */
    @Test
    void testParseModifierInfo() throws Exception {
        Method parseMod = LevelLoader.class.getDeclaredMethod("parseModifierInfo", String.class);
        parseMod.setAccessible(true);

        // 1. Test for null
        assertNull(parseMod.invoke(null, "."));

        // 2. Test items with a "Value" suffix
        Modifier adder = (Modifier) parseMod.invoke(null, "ADD:5");
        assertInstanceOf(Adder.class, adder);

        Modifier setter = (Modifier) parseMod.invoke(null, "SETSUT:H");
        assertInstanceOf(SuitSetter.class, setter);

        // 3. Test variables that do not have a "Value" suffix
        Modifier merger = (Modifier) parseMod.invoke(null, "MERGE");
        assertInstanceOf(Merger.class, merger);

        // 4. Testing for incorrect data formatting
        InvocationTargetException ex1 = assertThrows(InvocationTargetException.class, () -> parseMod.invoke(null, "ADD:5:6"));
        assertInstanceOf(IllegalArgumentException.class, ex1.getCause());

        // 5. Name "Modifier" does not actually exist
        InvocationTargetException ex2 = assertThrows(InvocationTargetException.class, () -> parseMod.invoke(null, "MAGIC_SPELL"));
        assertInstanceOf(IllegalArgumentException.class, ex2.getCause());
    }

}
