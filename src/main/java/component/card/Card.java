package component.card;

import util.GridIndexable;
import logic.GameLevel;
import util.GridPos;

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
    protected int value; // Valid value range from [1,13] (Ace -> 1, 2 -> 2, ... K -> 13)
    protected Material material;

    protected GridPos gridPos;

    private boolean isInfinite = false;

    // WARNING DO NOT CALL THIS FUNCTION TO CREATE A CARD
    // PLEASE LOOK INTO GameLevel.addCard() instead

    public Card() {
        // Default constructor is ace of spades.
        this(Suit.SPADE, 1, Material.PLASTIC);
    }

    public Card(Suit suit, int value, Material material) {
        this(suit, value, material, new GridPos(0, 0));
    }

    public Card(Suit suit, int value, Material material, int count) {
        this(suit, value, material);
        // hehe boii
        isInfinite = (count <= 0);
    }

    public Card(Suit suit, int value, Material material, GridPos gridPos) {
        this.gridPos = new GridPos();
        setSuit(suit);
        setValue(value);
        setMaterial(material);
        setGridPos(gridPos);
    }

    public boolean isInfiniteCard() {
        return isInfinite;
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

    public void setValue(int newValue) {
        // Convert to 0–12 space
        int zeroBased = newValue - 1;

        // Apply mathematical modulo
        zeroBased = ((zeroBased % 13) + 13) % 13;

        // Convert back to 1–13
        this.value = zeroBased + 1;
    }

    // GETTERS & SETTERS //

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    @Override
    public GridPos getGridPos() { return gridPos; }
    @Override
    public void setGridPos(GridPos point) {
        this.gridPos.setX(Math.clamp(point.getX(), 0, GameLevel.MAX_WIDTH));
        this.gridPos.setY(Math.clamp(point.getY(), 0, GameLevel.MAX_HEIGHT));
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
