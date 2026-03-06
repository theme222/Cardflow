package component.card;

import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.GridIndexable;
import logic.GameLevel;
import util.GridPos;
import util.Util;

/**
 * The {@code Card} class represents an individual entity within the game grid.
 * <p>
 * Each card carries a {@link Suit}, a numerical value, and a {@link Material}.
 * It implements {@link GridIndexable} for spatial tracking and {@link Tippable}
 * to provide contextual information when hovered by the user.
 */
public class Card implements GridIndexable, Tippable {

    protected Suit suit;
    /** Value range: [1, 13] (Ace to King). */
    protected int value;
    protected Material material;
    /** Health represents durability; primarily used by {@link Material#GLASS}. */
    protected int health;

    protected GridPos gridPos;

    /**
     * Default constructor. Creates an Ace of Spades made of Plastic.
     * <p>
     * <b>Warning:</b> Do not use this to manually inject cards into the game.
     * Use {@code GameLevel.addCard()} instead to ensure proper registry.
     */
    public Card() {
        this(Suit.SPADE, 1, Material.PLASTIC);
    }

    /**
     * Copy constructor. Creates a new card with properties identical to the source.
     * 
     * @param card The card to clone.
     */
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

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    /**
     * Sets the card value with circular wrapping logic.
     * <p>
     * Uses a mathematical modulo to ensure values outside the 1-13 range wrap
     * around.
     * For example, setting a value of 14 results in 1 (Ace), and 0 results in 13
     * (King).
     * 
     * @param newValue The desired card value.
     */
    public void setValue(int newValue) {
        int zeroBased = newValue - 1;
        // Apply true mathematical modulo to handle negative wraps correctly
        zeroBased = ((zeroBased % 13) + 13) % 13;
        this.value = zeroBased + 1;
    }

    /**
     * * Returns the material composition of the card.
     * 
     * @return The {@link Material} type currently applied to this card.
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Updates the card's material and adjusts health accordingly.
     * <p>
     * {@link Material#GLASS} cards are initialized with 3 health (breaking after 2
     * interactions),
     * while other materials default to -1 (invulnerable/standard).
     * 
     * @param material The new material to apply.
     */
    public void setMaterial(Material material) {
        this.material = material;
        this.setHealth(material == Material.GLASS ? 3 : -1);
    }

    /**
     * * Returns the current health (durability) of the card.
     * 
     * @return The health value. A value of -1 typically represents an
     *         indestructible state for non-fragile materials.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Updates the card's current health (durability) while enforcing a logical
     * lower bound.
     * <p>
     * This method uses {@code Math.clamp} to ensure the health value remains within
     * a valid range. The value {@code -1} is reserved as a sentinel indicating
     * <b>indestructibility</b> (standard for Plastic or Metal materials).
     * <p>
     * For fragile materials like {@code GLASS}, this value will typically be
     * positive
     * and decrement as the card interacts with modifiers, eventually leading to
     * the card's destruction if it hits {@code 0}.
     * * @param health The new health value to assigned to this card.
     */
    public void setHealth(int health) {
        this.health = Math.clamp(health, -1, Integer.MAX_VALUE);
    }

    /**
     * * Retrieves the current position of the card on the game grid.
     * <p>
     * Implementation of {@link GridIndexable#getGridPos()}.
     * 
     * @return The {@link GridPos} representing the card's X and Y coordinates.
     */
    @Override
    public GridPos getGridPos() {
        return gridPos;
    }

    /**
     * * Updates the card's location on the grid, enforcing level boundaries.
     * <p>
     * Implementation of {@link GridIndexable#setGridPos(GridPos)}. The coordinates
     * are clamped between (0, 0) and the maximum dimensions defined in
     * {@link GameLevel}.
     * 
     * @param point The target {@link GridPos} for the card.
     */
    @Override
    public void setGridPos(GridPos point) {
        this.gridPos.setX(Math.clamp(point.getX(), 0, GameLevel.MAX_WIDTH));
        this.gridPos.setY(Math.clamp(point.getY(), 0, GameLevel.MAX_HEIGHT));
    }

    /**
     * Returns whether this card blocks other entities.
     * 
     * @return Always {@code true}, as card stacking is prohibited.
     */
    @Override
    public boolean isBlocking() {
        return true;
    }

    /**
     * * Generates a string representation of the card for debugging.
     * 
     * @return A string containing the class name, suit, and value.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{" + suit + "," + value + '}';
    }

    /**
     * Compares two cards for logical equivalence.
     * <p>
     * <b>Note:</b> Standard {@code equals()} is not overridden to avoid
     * hash-collision
     * issues in collections where card identity (memory address) matters more than
     * value.
     * 
     * @param card          The card to compare against.
     * @param checkMaterial Whether the material type must also match.
     * @return {@code true} if the cards are equivalent.
     */
    public boolean isEquivalent(Card card, boolean checkMaterial) {
        if (card == null)
            return false;
        return this.suit == card.getSuit() &&
                this.value == card.getValue() &&
                (!checkMaterial || this.material == card.getMaterial());
    }

    /**
     * Compares two cards for logical equivalence. Does not compare materials.
     * <p>
     * <b>Note:</b> Standard {@code equals()} is not overridden to avoid
     * hash-collision
     * issues in collections where card identity (memory address) matters more than
     * value.
     * 
     * @param card The card to compare against.
     * @return {@code true} if the cards are equivalent.
     */
    public boolean isEquivalent(Card card) {
        return isEquivalent(card, false);
    }

    /**
     * Generates a descriptive tooltip for the UI.
     * 
     * @return A {@link Tooltip} containing the card's name, material, and value
     *         details.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                Util.getValueAsDetailedString(getValue()) + " Of " + getSuit().toString() + "s",
                Color.MEDIUMVIOLETRED,
                "A card made out of ",
                Tooltip.ref(getMaterial()),
                ". It has the value of ",
                Tooltip.ref(getValue()),
                " with a ",
                Tooltip.ref(getSuit()),
                " suit.");
    }
}