package util;

import component.card.Card;
import component.modifier.changer.*;
import component.modifier.combinator.Merger;
import component.modifier.combinator.Splitter;
import component.modifier.combinator.Vaporizer;
import logic.GameLevel;
import component.GameTile;
import component.modifier.Modifier;
import component.modifier.pathway.Entrance;
import component.modifier.pathway.Exit;

import javax.json.*;
import java.awt.*;
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

    public static final int TOTAL_LEVELS = 20;

    private static Card.Suit parseSuit(String suitString) {
        return switch (suitString.toUpperCase()) {
            case "HEART", "H" -> Card.Suit.HEART;
            case "CLUB", "C" -> Card.Suit.CLUB;
            case "DIAMOND", "D" -> Card.Suit.DIAMOND;
            case "SPADE", "S" -> Card.Suit.SPADE;
            default -> throw new IllegalArgumentException("Invalid suit");
        };
    }

    private static Card.Material parseMaterial(String materialString) {
        return switch (materialString.toUpperCase()) {
            case "PLASTIC", "P" -> Card.Material.PLASTIC;
            case "STONE", "S" ->  Card.Material.STONE;
            case "GLASS", "G" ->  Card.Material.GLASS;
            case "METAL", "M" ->  Card.Material.METAL;
            case "CORRUPTED", "C" -> Card.Material.CORRUPTED;
            case "RUBBER", "R" -> Card.Material.RUBBER;
            default -> throw new IllegalArgumentException("Invalid material");
        };

    }

    private static Card parseCardInfo(JsonObject cardJson, boolean hasMaterial) {
        Card.Suit suit = parseSuit(cardJson.getString("suit"));
        int value = cardJson.getInt("value");
        Card.Material material = hasMaterial ? parseMaterial(cardJson.getString("material")):  Card.Material.PLASTIC;
        int count = cardJson.containsKey("count") ? cardJson.getInt("count") : 1;

        return new Card(suit, value, material, count);
    }

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
            case "SETSUT" -> new SuitSetter(parseSuit(value));
            case "SETMAT" -> new MaterialSetter(parseMaterial(value));
            case "SETNUM" -> new NumberSetter(Integer.parseInt(value));
            case "VAP" -> new Vaporizer();
            case "MERGE" -> new Merger();
            case "SPLIT" -> new Splitter();
            case "ENTER" -> new Entrance();
            case "EXIT" -> new Exit();
            default -> throw new IllegalArgumentException("Invalid modifier " + modifier);
        };
    }

    private static void parseMoverInfo(JsonObject moverJson, HashMap<String, Integer> outMap) {
        String moverClassName = moverJson.getString("name").toUpperCase();
        int moverCount = moverJson.getInt("count");

        String[] validClassNames = { "CONVEYOR", "FLIPFLOP" };
        if (moverCount < -1)
            throw new IllegalArgumentException("Invalid mover count " + moverCount); // -1 for infinity

        boolean isValid = false;
        for (String validClassName : validClassNames)
            isValid = isValid || validClassName.equals(moverClassName);

        if (!isValid)
            throw new IllegalArgumentException("Invalid mover name " + moverClassName);

        outMap.put(moverClassName, moverCount);
    }

    public static GameLevel loadLevel(int levelNumber) throws IOException {
        // feel free to change this because honestly it's a headache to look at

        if (levelNumber <= 0 || levelNumber > TOTAL_LEVELS)
            throw new IllegalArgumentException("Invalid level number " + levelNumber);

        String basePath = "levels/" + levelNumber;

        try ( // https://www.w3schools.com/java/java_try_catch_resources.asp
                InputStream configStream = LevelLoader.class
                        .getClassLoader()
                        .getResourceAsStream(basePath + "/config.json");

                InputStream layoutStream = LevelLoader.class
                        .getClassLoader()
                        .getResourceAsStream(basePath + "/level.tsv");

                BufferedReader csvReader = new BufferedReader(
                        new InputStreamReader(layoutStream, StandardCharsets.UTF_8));

                JsonReader jsonReader = Json.createReader(configStream);) {

            // ---------- JSON parsing ---------- //

            JsonObject jsonObject = jsonReader.readObject();

            String levelName = jsonObject.getString("name");
            int levelWidth = jsonObject.getInt("width");
            int levelHeight = jsonObject.getInt("height");

            List<Card> inputCards = new ArrayList<>();
            List<Card> outputCards = new ArrayList<>();

            for (JsonValue value : jsonObject.getJsonArray("inputCards"))
                inputCards.add(parseCardInfo(value.asJsonObject(), true));

            for (JsonValue value : jsonObject.getJsonArray("outputCards"))
                outputCards.add(parseCardInfo(value.asJsonObject(), false));

            HashMap<String, Integer> availableMovers = new HashMap<>(); // Using the classname to store this

            for (JsonValue value : jsonObject.getJsonArray("availableMovers"))
                parseMoverInfo(value.asJsonObject(), availableMovers);

            // ---------- JSON parsing ---------- //

            // ---------- CSV parsing ---------- //

            List<String> lines = csvReader.lines().toList();

            if (lines.size() != levelHeight)
                throw new IllegalStateException("CSV row count does not match grid height");

            GameTile[][] grid = new GameTile[levelHeight][levelWidth];
            HashSet<Modifier> modifiers = new HashSet<>();

            for (int y = 0; y < levelHeight; y++) {
                String[] cells = lines.get(y).split("\t");

                if (cells.length != levelWidth) {
                    throw new IllegalStateException(
                            "CSV column count mismatch at row " + y);
                }

                for (int x = 0; x < levelWidth; x++) {
                    String token = cells[x].trim();
                    Modifier mod = parseModifierInfo(token);
                    grid[y][x] = new GameTile(mod, x, y);
                    if (mod != null) {
                        mod.setGridPos(new GridPos(x, y));
                        modifiers.add(mod);
                    }
                }
            }

            // ---------- CSV parsing ---------- //

            return new GameLevel(
                    levelName,
                    levelWidth,
                    levelHeight,
                    inputCards,
                    outputCards,
                    availableMovers,
                    grid,
                    modifiers);

        }

    }

    public static GameLevel loadSandboxLevel() throws IOException {
        String basePath = "levels/sandbox";

        try (
                InputStream configStream = LevelLoader.class
                        .getClassLoader()
                        .getResourceAsStream(basePath + "/config.json");

                InputStream layoutStream = LevelLoader.class
                        .getClassLoader()
                        .getResourceAsStream(basePath + "/level.tsv");

                BufferedReader csvReader = new BufferedReader(
                        new InputStreamReader(layoutStream, StandardCharsets.UTF_8));

                JsonReader jsonReader = Json.createReader(configStream);) {
            // 🔁 reuse exact same logic
            JsonObject jsonObject = jsonReader.readObject();

            String levelName = jsonObject.getString("name");
            int levelWidth = jsonObject.getInt("width");
            int levelHeight = jsonObject.getInt("height");

            List<Card> inputCards = new ArrayList<>();
            List<Card> outputCards = new ArrayList<>();

            for (JsonValue value : jsonObject.getJsonArray("inputCards"))
                inputCards.add(parseCardInfo(value.asJsonObject(), true));

            for (JsonValue value : jsonObject.getJsonArray("outputCards"))
                outputCards.add(parseCardInfo(value.asJsonObject(), false));

            HashMap<String, Integer> availableMovers = new HashMap<>();
            for (JsonValue value : jsonObject.getJsonArray("availableMovers"))
                parseMoverInfo(value.asJsonObject(), availableMovers);

            List<String> lines = csvReader.lines().toList();
            GameTile[][] grid = new GameTile[levelHeight][levelWidth];
            HashSet<Modifier> modifiers = new HashSet<>();

            for (int y = 0; y < levelHeight; y++) {
                String[] cells = lines.get(y).split("\t");
                for (int x = 0; x < levelWidth; x++) {
                    Modifier mod = parseModifierInfo(cells[x].trim());
                    grid[y][x] = new GameTile(mod, x, y);
                    if (mod != null) {
                        mod.setGridPos(new GridPos(x, y));
                        modifiers.add(mod);
                    }
                }
            }

            return new GameLevel(
                    "[SANDBOX] " + levelName,
                    levelWidth,
                    levelHeight,
                    inputCards,
                    outputCards,
                    availableMovers,
                    grid,
                    modifiers);
        }
    }

}
