package logic.event.card;

import java.util.function.BiFunction;

import component.GameTile;
import component.mover.Mover;
import logic.event.LogicEvent;
import util.Direction;

public class TileSelectChangeEvent extends LogicEvent{
    final private String tile;
    final private Direction rotation;
    final private BiFunction<String,Direction,Mover> factory;

    public TileSelectChangeEvent(String tile, Direction rotation, BiFunction<String,Direction,Mover> factory) {
        super();
        this.tile = tile;
        this.rotation = rotation;
        this.factory = factory;
    }
    
    public String getMovements() {
        return tile;
    }

    public Direction getRotation(){
        return rotation;
    }

    public BiFunction<String,Direction,Mover> getFactory() {
        return factory;
    }
}