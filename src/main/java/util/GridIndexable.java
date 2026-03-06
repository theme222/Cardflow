package util;

/**
 * The {@code GridIndexable} interface is implemented by any object 
 * that occupies a position on the game grid.
 * <p>
 * It provides methods for accessing and modifying the object's 
 * coordinates, as well as determining its physical presence (blocking).
 */
public interface GridIndexable {

    /** @return The current {@link GridPos} of the object. */
    GridPos getGridPos();

    /** @param gridPos The new {@link GridPos} to assign to this object. */
    void setGridPos(GridPos gridPos);

    /** @return {@code true} if this object blocks movement into its grid cell. */
    boolean isBlocking(); // This varies between classes
}
