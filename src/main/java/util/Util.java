package util;

public class Util {
    public static String getValueAsString(int value) {
        // Always returns the lowercase version
        return switch (value) {
            case 1 -> "a";
            case 11 -> "j";
            case 12 -> "q";
            case 13 -> "k";
            default -> String.valueOf(value);
        };
    }

    public static String getValueAsDetailedString(int value) {
        String[] detailedStrings = {"Zero", "Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        return detailedStrings[value]; // not my finest work but it'll do :/
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
