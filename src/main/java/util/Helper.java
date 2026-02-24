package util;

public class Helper {
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
}
