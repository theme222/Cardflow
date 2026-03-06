package logic.event.card;

import java.util.function.BiFunction;

import component.GameTile;
import component.mover.Mover;
import logic.event.LogicEvent;
import util.Direction;

/**
 * Event fired when the player's selected tile type or rotation changes in the inventory.
 */
public class TileSelectChangeEvent extends LogicEvent{
    /** The name of the selected tile type. */
    final private String tile;
    /** The currently selected rotation direction. */
    final private Direction rotation;
    /** A factory function to create {@link Mover} instances. */
    final private BiFunction<String,Direction,Mover> factory;

    /**
     * Constructs a new TileSelectChangeEvent.
     * 
     * @param tile The tile name.
     * @param rotation The rotation direction.
     * @param factory The mover factory function.
     */
    public TileSelectChangeEvent(String tile, Direction rotation, BiFunction<String,Direction,Mover> factory) {
        super();
        this.tile = tile;
        this.rotation = rotation;
        this.factory = factory;
    }
    
    /** 
     * Gets the name of the selected tile.
     * 
     * @return The tile name.
     */
    public String getMovements() {
        return tile;
    }

    /** 
     * Gets the current rotation.
     * 
     * @return The {@link Direction}.
     */
    public Direction getRotation(){
        return rotation;
    }

    /** 
     * Gets the factory function for creating movers.
     * 
     * @return A {@link BiFunction} that takes a name and direction and returns a {@link Mover}.
     */
    public BiFunction<String,Direction,Mover> getFactory() {
        return factory;
    }
}
