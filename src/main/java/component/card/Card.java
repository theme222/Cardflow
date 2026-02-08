package component.card;

import component.GridIndexable;
import logic.level.GameLevel;

import java.awt.*;

public class Card implements GridIndexable {

    public enum Suit {
        HEART,
        DIAMOND,
        CLUB,
        SPADE
    }

    public enum Material { // The reason why I didn't do inheritance is that the material is an attribute that changes often.
        PLASTIC,
        METAL,
        STONE,
        GLASS,
        RUBBER,
        CORRUPTED
    }

    protected Suit suit;
    protected int value; // Valid value range from [0,12] (Ace -> 0, 2 -> 1, ... K -> 12)
    protected Material material;

    protected Point gridPos;

    // WARNING DO NOT CALL THIS FUNCTION TO CREATE A CARD
    // PLEASE LOOK INTO GameLevel.addCard() instead

    public Card() {
        // Default constructor is ace of spades.
        this(Suit.SPADE, 1, Material.PLASTIC);
    }

    public Card(Suit suit, int value, Material material) {
        this(suit, value, material, new Point(0, 0));
    }

    public Card(Suit suit, int value, Material material, Point gridPos) {
        this.gridPos = new Point();
        setSuit(suit);
        setValue(value);
        setMaterial(material);
        setGridPos(gridPos);
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        // Value wraps around if overflow or underflow
        value = Math.floorMod(value, 13);
        this.value = value;
    }

    // GETTERS & SETTERS //

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    @Override
    public Point getGridPos() { return gridPos; }
    @Override
    public void setGridPos(Point point) {
        point = point == null ? new Point() : point;
        this.gridPos.x = Math.clamp(point.x, 0, GameLevel.MAX_WIDTH);
        this.gridPos.y = Math.clamp(point.y, 0, GameLevel.MAX_HEIGHT);
    }

    // GETTERS & SETTERS //

    @Override
    public boolean isBlocking() {
        return true; // Always true (No card stacking allowed)
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "{" +
                suit +
                "," + value +
                '}';
    }
}
