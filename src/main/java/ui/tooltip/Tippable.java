package ui.tooltip;

/**
 * The {@code Tippable} interface should be implemented by any object 
 * that can provide a {@link Tooltip} for display.
 */
public interface Tippable {
    /** @return The {@link Tooltip} associated with this object. */
    Tooltip getTooltip();
}
