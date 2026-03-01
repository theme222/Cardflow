package component.mover;

import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.GridIndexable;
import logic.GameLevel;
import util.*;

abstract public class Mover implements GridIndexable, Tippable {


    public static GridPos getTranslationFromDirection(Direction direction) {
        return switch (direction) {
            case UP-> new GridPos(0, -1);
            case DOWN-> new GridPos(0, 1);
            case LEFT-> new GridPos(-1, 0);
            case RIGHT-> new GridPos(1, 0);
            default -> new GridPos(0,0);
        };
    }


    public abstract Direction getDirectionStateless(); // This will get the direction where it will send without potentially modifying the internals (like flipflop or delay that changes when you read it)

    public Direction getDirection() { // This is what the game will read from when deciding where to send the card.
        return getDirectionStateless();
    }

    protected GridPos gridPos;
    protected Direction rotation; // Ensure never STAY

    public Mover(Direction rot) {
        this.gridPos = new GridPos();
        setGridPos(gridPos);
        setRotation(rot);
    }


    public Direction getRotation() {
        return rotation;
    }

    public void setRotation(Direction rot) {
        if (rot == Direction.STAY) rot = Direction.UP;
        this.rotation = rot;
    }

    public abstract Direction[] getValidOutputDirections();

    public void rotate() {
        setRotation(this.rotation.next());
    }

    @Override
    public GridPos getGridPos() { return gridPos; }
    @Override
    public void setGridPos(GridPos point) {
        this.gridPos.setX(Math.clamp(point.getX(), 0, GameLevel.MAX_WIDTH));
        this.gridPos.setY(Math.clamp(point.getY(), 0, GameLevel.MAX_HEIGHT));
    }

    public boolean isBlocking() {return false;}

    public void reset() { }

    @Override
    public Tooltip getTooltip() {
        return getMoverTooltip();
    }

    public static Tooltip getMoverTooltip() {
        return new Tooltip("Mover",
                Color.OLIVEDRAB, // Genuinely tho what is this color???
                "Facilitates the transportation of cards"
        );
    }
}
