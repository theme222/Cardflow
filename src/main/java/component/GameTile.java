package component;

import component.card.Card;
import component.modifier.Modifier;
import component.mover.Mover;
import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.GridIndexable;
import util.GridPos;

/**
 * The {@code GameTile} represents a single cell in the game grid.
 * <p>
 * It functions as a data structure that holds references to the different entities
 * currently occupying its coordinate. It handles the presence of a {@link Card}, 
 * a {@link Modifier}, and a {@link Mover} simultaneously.
 */
public class GameTile implements Tippable {

    private Card card;
    private Modifier modifier;
    private Mover mover;

    /** The immutable X-coordinate of this tile on the grid. */
    public final int posX;
    /** The immutable Y-coordinate of this tile on the grid. */
    public final int posY;

    /**
     * Constructs a new GameTile at a specific coordinate.
     * @param modifier The initial logic {@link Modifier} placed on this tile.
     * @param posX The grid X-position.
     * @param posY The grid Y-position.
     */
    public GameTile(Modifier modifier, int posX, int posY) {
        this.modifier = modifier;
        this.posX = posX;
        this.posY = posY;
    }

    // --- Component Management ---

    /** @return The {@link Card} currently on this tile, or {@code null} if empty. */
    public Card getCard() { return card; }
    /** @param card The {@link Card} to place on this tile. */
    public void setCard(Card card) { this.card = card; }

    /** @return The {@link Modifier} assigned to this tile. */
    public Modifier getModifier() { return modifier; }
    /** @param modifier The {@link Modifier} to set for this tile. */
    public void setModifier(Modifier modifier) { this.modifier = modifier; }

    /** @return The {@link Mover} assigned to this tile. */
    public Mover getMover() { return mover; }
    /** @param mover The {@link Mover} to set for this tile. */
    public void setMover(Mover mover) { this.mover = mover; }

    /**
     * @return A new {@link GridPos} representing this tile's coordinates.
     */
    public GridPos getGridPos() {
        return new GridPos(posX, posY);
    }

    // --- Dynamic Type Dispatching ---

    /** * Retrieves the component of the same class as the provided template.
     * <p>
     * This is useful for generic systems that handle {@link GridIndexable} types
     * without knowing explicitly which layer they are targeting.
     * @param g A template object to match the class.
     * @return The matching component instance on this tile.
     * @throws IllegalArgumentException if the type is not Card, Modifier, or Mover.
     */
    public GridIndexable getSameClassOnTile(GridIndexable g) {
        if (g == null) return null;
        if (g instanceof Card) return card;
        if (g instanceof Modifier) return modifier;
        if (g instanceof Mover) return mover;
        throw new IllegalArgumentException("Unknown GridIndexable");
    }

    /** * Sets a component on the tile by detecting its class type.
     * @param g The {@link GridIndexable} entity to place on the tile.
     */
    public void setSameClassOnTile(GridIndexable g) {
        if (g instanceof Card) card = (Card) g;
        else if (g instanceof Modifier) modifier = (Modifier) g;
        else if (g instanceof Mover) mover = (Mover) g;
        else if (g != null) throw new IllegalArgumentException("Unknown GridIndexable");
    }

    /** * Clears the reference for the specific layer matching the object's type.
     * @param g The object whose type determines which layer to nullify.
     */
    public void removeSameClassOnTile(GridIndexable g) {
        if (g instanceof Card) card = null;
        else if (g instanceof Modifier) modifier = null;
        else if (g instanceof Mover) mover = null;
        else if (g != null) throw new IllegalArgumentException("Unknown GridIndexable");
    }

    /** * Returns a string representation of the tile's occupants.
     * @return A string in the format {Card, Modifier, Mover}.
     */
    @Override
    public String toString() {
        return "{" + card + ", " + modifier + ", " + mover + '}';
    }

    /** * Aggregates tooltips from all active components on this tile.
     * @return A combined {@link Tooltip} for the card, modifier, and mover.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            null,
            Color.BLACK,
            Tooltip.ref(getCard()),
            Tooltip.ref(getModifier()),
            Tooltip.ref(getMover())
        );
    }
}