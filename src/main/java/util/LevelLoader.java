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

/**
 * The {@code LevelLoader} class is responsible for parsing level configuration 
 * and layout files from the application resources.
 * <p>
 * It handles the conversion of JSON and TSV data into {@link GameLevel} objects, 
 * including card specifications, tile modifiers, and available player inventory.
 */
public class LevelLoader {

    /** The total number of playable levels in the game. */
    public static final int TOTAL_LEVELS = 16;

    /** 
     * Parses a string representation of a card suit.
     * @param suitString The suit name or shorthand (H, C, D, S).
     * @return The corresponding {@link Suit} enum.
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
     * Parses a string representation of a card material.
     * @param materialString The material name or shorthand.
     * @return The corresponding {@link Material} enum.
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
     * Extracts card specifications from a JSON object.
     * @param cardJson The JSON containing suit, value, material, and count.
     * @return A {@link CardCount} object representing the card(s).
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
     * Creates a modifier instance based on a string descriptor.
     * <p>
     * Format is typically "TYPE:VALUE" (e.g., "ADD:5", "SETSUT:H").
     * @param modifier The string descriptor from the level layout file.
     * @return A {@link Modifier} instance, or {@code null} if empty (".").
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
     * Parses information about available mover tools for the player's inventory.
     * @param moverJson The JSON object defining the mover type and quantity.
     * @param outMap The map to store the parsed mover counts.
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
     * Loads a level by its identifier (e.g., "1", "sandbox").
     * @param level The name of the level folder to load.
     * @return A fully initialized {@link GameLevel} object.
     * @throws IOException If the configuration or layout files cannot be read.
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
     * Parses the general metadata and card/inventory requirements from a level config JSON.
     * @param json The root JSON object of the config.json file.
     * @return A {@code LevelConfig} record containing the parsed metadata.
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
     * Parses the grid layout and pre-placed modifiers from a TSV file.
     * @param reader The reader for the level.tsv file.
     * @param width The expected grid width.
     * @param height The expected grid height.
     * @return A {@code LayoutData} record containing the grid and modifier set.
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

    /** Inner record to hold raw configuration data before level assembly. */
    private record LevelConfig(
            String name,
            int width,
            int height,
            List<CardCount> inputCards,
            List<CardCount> outputCards,
            HashMap<String, Integer> availableMovers) {
    }

    /** Inner record to hold grid and modifier data during layout parsing. */
    private record LayoutData(
            GameTile[][] grid,
            HashSet<Modifier> modifiers) {
    }

}
