package placement;

public abstract class TileConstructor<T> {

    /** Called when hovering / previewing */
    public abstract void preview();

    /** Build the final object */
    public abstract T construct();

    /** Cycle orientation / variant */
//    public abstract void cycleVariant(); // I feel like rotation should be univers
}
