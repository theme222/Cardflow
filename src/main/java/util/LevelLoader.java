package util;

import component.card.Card;
import component.card.Material;
import component.card.Suit;
import component.modifier.changer.*;
import component.modifier.combinator.*;
import logic.GameLevel;
import component.GameTile;
import component.modifier.Modifier;
import component.modifier.pathway.Entrance;
import component.modifier.pathway.Exit;

import javax.json.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LevelLoader {

    public static final int TOTAL_LEVELS = 16;

    /** 
     * @param suitString
     * @return Suit
     */
    private static Suit parseSuit(String suitString) {
        return switch (suitString.toUpperCase()) {
            case "HEART", "H" -> Suit.HEART;
            case "CLUB", "C" -> Suit.CLUB;
            case "DIAMOND", "D" -> Suit.DIAMOND;
            case "SPADE", "S" -> Suit.SPADE;
            default -> throw new IllegalArgumentException("Invalid suit");
        };
    }

    /** 
     * @param materialString
     * @return Material
     */
    private static Material parseMaterial(String materialString) {
        return switch (materialString.toUpperCase()) {
            case "PLASTIC", "P" -> Material.PLASTIC;
            case "STONE", "S" -> Material.STONE;
            case "GLASS", "G" -> Material.GLASS;
            case "METAL", "M" -> Material.METAL;
            case "CORRUPTED", "C" -> Material.CORRUPTED;
            case "RUBBER", "R" -> Material.RUBBER;
            default -> throw new IllegalArgumentException("Invalid material");
        };

    }

    /** 
     * @param cardJson
     * @return CardCount
     */
    private static CardCount parseCardInfo(JsonObject cardJson) {
        Suit suit = parseSuit(cardJson.getString("suit"));
        int value = cardJson.getInt("value");
        Material material = cardJson.containsKey("material") ? parseMaterial(cardJson.getString("material"))
                : Material.PLASTIC;
        int count = cardJson.containsKey("count") ? cardJson.getInt("count") : 1;

        return new CardCount(new Card(suit, value, material), count);
    }

    /** 
     * @param modifier
     * @return Modifier
     */
    private static Modifier parseModifierInfo(String modifier) {
        String[] modArray = modifier.split(":");
        String modName = modArray[0];
        String value = "";

        if (modArray.length == 2) {
            value = modArray[1];
        } else if (modArray.length >= 3) {
            throw new IllegalArgumentException("Invalid modifier " + modifier);
        }

        return switch (modName) {
            case "." -> null;
            case "ADD" -> new Adder(Integer.parseInt(value));
            case "SUB" -> new Subtractor(Integer.parseInt(value));
            case "MUL" -> new Multiplier(Integer.parseInt(value));
            case "DIV" -> new Divider(Integer.parseInt(value));
            case "SETSUT" -> new SuitSetter(parseSuit(value));
            case "SETMAT" -> new MaterialSetter(parseMaterial(value));
            case "SETVAL" -> new ValueSetter(Integer.parseInt(value));
            case "VAP" -> new Vaporizer();
            case "MERGE" -> new Merger();
            case "SPLIT" -> new Splitter();
            case "DUP" -> new Duplicator();
            case "ABS" -> new Absorber();
            case "ENTER" -> new Entrance();
            case "EXIT" -> new Exit();
            default -> throw new IllegalArgumentException("Invalid modifier " + modifier);
        };
    }

    /** 
     * @param moverJson
     * @param outMap
     */
    private static void parseMoverInfo(JsonObject moverJson, HashMap<String, Integer> outMap) {
        String moverClassName = moverJson.getString("name").toUpperCase();
        int moverCount = moverJson.getInt("count");

        String[] validClassNames = { "CONVEYOR", "FLIPFLOP", "PARITYFILTER", "REDBLACKFILTER", "DELAY" };
        if (moverCount < -1)
            throw new IllegalArgumentException("Invalid mover count " + moverCount); // -1 for infinity

        boolean isValid = false;
        for (String validClassName : validClassNames)
            isValid = isValid || validClassName.equals(moverClassName);

        if (!isValid)
            throw new IllegalArgumentException("Invalid mover name " + moverClassName);

        outMap.put(moverClassName, moverCount);
    }

    /** 
     * @param level
     * @return GameLevel
     * @throws IOException
     */
    public static GameLevel loadLevel(String level) throws IOException {

        String basePath = "levels/" + level;

        InputStream configStream = LevelLoader.class.getClassLoader().getResourceAsStream(basePath + "/config.json");

        InputStream layoutStream = LevelLoader.class.getClassLoader().getResourceAsStream(basePath + "/level.tsv");

        if (configStream == null || layoutStream == null)
            throw new IllegalArgumentException("Failed to load level " + level);

        try (
                BufferedReader layoutReader = new BufferedReader(
                        new InputStreamReader(layoutStream, StandardCharsets.UTF_8));

                JsonReader jsonReader = Json.createReader(configStream);) {

            LevelConfig config = parseConfig(jsonReader.readObject());

            LayoutData layout = parseLayout(layoutReader, config.width, config.height);

            return new GameLevel(
                    level,
                    config.name,
                    config.width,
                    config.height,
                    config.inputCards,
                    config.outputCards,
                    config.availableMovers,
                    layout.grid,
                    layout.modifiers);
        }
    }

    /** 
     * @param json
     * @return LevelConfig
     */
    private static LevelConfig parseConfig(JsonObject json) {

        String levelName = json.getString("name");
        int levelWidth = json.getInt("width");
        int levelHeight = json.getInt("height");

        List<CardCount> inputCards = new ArrayList<>();
        List<CardCount> outputCards = new ArrayList<>();

        for (JsonValue value : json.getJsonArray("inputCards"))
            inputCards.add(parseCardInfo(value.asJsonObject()));

        for (JsonValue value : json.getJsonArray("outputCards"))
            outputCards.add(parseCardInfo(value.asJsonObject()));

        HashMap<String, Integer> availableMovers = new HashMap<>();

        for (JsonValue value : json.getJsonArray("availableMovers"))
            parseMoverInfo(value.asJsonObject(), availableMovers);

        return new LevelConfig(
                levelName,
                levelWidth,
                levelHeight,
                inputCards,
                outputCards,
                availableMovers);
    }

    /** 
     * @param reader
     * @param width
     * @param height
     * @return LayoutData
     */
    private static LayoutData parseLayout(
            BufferedReader reader,
            int width,
            int height) {

        List<String> lines = reader.lines().toList();

        if (lines.size() != height)
            throw new IllegalStateException("CSV row count mismatch");

        GameTile[][] grid = new GameTile[height][width];
        HashSet<Modifier> modifiers = new HashSet<>();

        for (int y = 0; y < height; y++) {

            String[] cells = lines.get(y).split("\t");

            if (cells.length != width)
                throw new IllegalStateException("CSV column mismatch at row " + y);

            for (int x = 0; x < width; x++) {

                Modifier mod = parseModifierInfo(cells[x].trim());

                grid[y][x] = new GameTile(mod, x, y);

                if (mod != null) {
                    mod.setGridPos(new GridPos(x, y));
                    modifiers.add(mod);
                }
            }
        }

        return new LayoutData(grid, modifiers);
    }

    private record LevelConfig(
            String name,
            int width,
            int height,
            List<CardCount> inputCards,
            List<CardCount> outputCards,
            HashMap<String, Integer> availableMovers) {
    }

    private record LayoutData(
            GameTile[][] grid,
            HashSet<Modifier> modifiers) {
    }

}
