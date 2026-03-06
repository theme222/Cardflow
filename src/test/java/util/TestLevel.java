package util;

import component.GameTile;
import component.modifier.Modifier;
import logic.GameLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TestLevel {
    /** 
     * @return GameLevel
     */
    public static GameLevel initGlobalGameLevel() {
        int width = 5;
        int height = 5;

        List<CardCount> inputCards = new ArrayList<>();
        List<CardCount> outputCards = new ArrayList<>();
        HashMap<String, Integer> availableMovers = new HashMap<>();
        HashSet<Modifier> modifierSet = new HashSet<>();
        GameTile[][] grid = new GameTile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new GameTile(null, x, y);
            }
        }
        //initialize level
        GameLevel level = new GameLevel("test", "test", width, height, inputCards, outputCards, availableMovers, grid, modifierSet);
        GameLevel.setInstance(level);
        return level;
    }
}
