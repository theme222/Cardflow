import static org.junit.jupiter.api.Assertions.*;

import logic.GameLevel;
import util.LevelLoader;
import org.junit.jupiter.api.Test;

public class TestLoader {

    @Test
    void testLoadLevel() {
        System.out.println("Loading level");

        assertDoesNotThrow(() -> {
            GameLevel gameLevel = LevelLoader.loadLevel(1);
            System.out.println(gameLevel);
        });

        assertThrows(Exception.class, () -> {
            GameLevel gameLevel = LevelLoader.loadLevel(-1);
        });
    }



}
