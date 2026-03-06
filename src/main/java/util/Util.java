package util;

/**
 * The {@code Util} class provides miscellaneous static helper methods 
 * for string manipulation and value formatting.
 */
public class Util {
    /** 
     * Converts a card's numerical value to its shorthand string representation.
     * <p>
     * Special values: 1 -> "a", 11 -> "j", 12 -> "q", 13 -> "k".
     * 
     * @param value The numerical value of the card.
     * @return A single-character string representing the value.
     */
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

    /** 
     * Converts a numerical value to its full English word representation.
     * 
     * @param value The numerical value (0-13).
     * @return The word for the number (e.g., "Ace", "King").
     */
    public static String getValueAsDetailedString(int value) {
        String[] detailedStrings = {"Zero", "Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
        return detailedStrings[value]; // not my finest work but it'll do :/
    }

    /** 
     * Capitalizes the first letter of a string and lowercases the rest.
     * 
     * @param str The string to format.
     * @return The capitalized string.
     */
    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
