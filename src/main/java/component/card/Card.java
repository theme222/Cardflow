package component.card;

import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.GridIndexable;
import logic.GameLevel;
import util.GridPos;
import util.Util;

public class Card implements GridIndexable, Tippable {

    protected Suit suit;
    protected int value; // Valid value range from [1,13] (Ace -> 1, 2 -> 2, ... K -> 13)
    protected Material material;
    protected int health; // Only really valid on Glass material (Gets set during material change or modified)

    protected GridPos gridPos;

    // WARNING DO NOT CALL THIS FUNCTION TO CREATE A CARD
    // PLEASE LOOK INTO GameLevel.addCard() instead

    public Card() {
        // Default constructor is ace of spades.
        this(Suit.SPADE, 1, Material.PLASTIC);
    }

    public Card(Card card) {
        this(card.getSuit(), card.getValue(), card.getMaterial(), card.getGridPos());
    }

    public Card(Suit suit, int value, Material material) {
        this(suit, value, material, new GridPos(0, 0));
    }

    public Card(Suit suit, int value, Material material, GridPos gridPos) {
        this.gridPos = new GridPos();
        setSuit(suit);
        setValue(value);
        setMaterial(material);
        setGridPos(gridPos);
    }


    // GETTERS & SETTERS //

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

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) {
        this.material = material;
        this.setHealth(material == Material.GLASS ? 3: -1); // glass can go through 2 modifiers without breaking
    }

    public int getHealth() { return health; }
    public void setHealth(int health) {
        this.health = Math.clamp(health, -1, Integer.MAX_VALUE);
    }

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

    // !! NOT Overriding equals DUE TO HASH ISSUES USE isEquivalent INSTEAD !! //

    public boolean isEquivalent(Card card, boolean checkMaterial) {
        return this.getSuit() == card.getSuit() &&
                this.getValue() == card.getValue() &&
                (checkMaterial ? this.getMaterial() == card.getMaterial(): true);
    }

    public boolean isEquivalent(Card card) {
        if (card == null) return false;
        return isEquivalent(card, false);
    }

    // !! NOT Overriding equals DUE TO HASH ISSUES USE isEquivalent INSTEAD !! //

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                Util.getValueAsDetailedString(getValue()) + " Of " + getSuit().toString() + "s",
                Color.MEDIUMVIOLETRED,
                "A card made out of ",
                Tooltip.ref(getMaterial()),
                ". It has the value of ",
                Tooltip.ref(getValue()), // yes this displays a number
                " with a ",
                Tooltip.ref(getSuit()),
                " suit."
        );
    }
}
