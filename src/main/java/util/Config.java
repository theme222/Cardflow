package util;

/**
 * The {@code Config} class centralizes all global constants used throughout 
 * the application, including UI sizing, timing, and styling parameters.
 */
public final class Config {
    /** The standard size for a single grid tile in pixels. */
    public static final int TILE_SIZE = 85; // px
    /** The default transparency level for modifier tile icons. */
    public static final double MODIFIER_ALPHA = 0.67;

    /** The primary font used for titles and general UI elements. */
    public static final String REGULAR_FONT = "Pixelated Elegance";
    /** The font used for numbers and technical displays. */
    public static final String MONOSPACE_FONT = "Mozart NBP";

    /** The time interval between engine ticks in milliseconds. */
    public static final long TICK_DURATION_MS = 500;

}
