package component.modifier;

import component.card.Card;
import component.card.Material;
import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.GridIndexable;
import logic.GameLevel;
import util.GridPos;

/**
 * The {@code Modifier} class is the abstract foundation for all interactive 
 * board components.
 * <p>
 * It manages the physical location on the grid, the operational state 
 * (active/disabled), and universal card material interactions like 
 * <b>Corruption</b> and <b>Fragility</b>.
 */
abstract public class Modifier implements GridIndexable, Tippable {
    private boolean isDisabled;
    protected GridPos gridPos;

    /**
     * Default constructor for a {@code Modifier}.
     * <p>
     * Initializes the modifier at a default {@link GridPos} (typically 0,0) 
     * and sets its state to enabled. This is primarily used for temporary 
     * instances or when the position will be set immediately after instantiation.
     */
    public Modifier() {
        this(new GridPos());
    }

    /**
     * Constructs a {@code Modifier} at a specific location on the game grid.
     * <p>
     * This constructor handles the internal initialization of the position object 
     * and ensures the modifier starts in an active (non-disabled) state. The 
     * provided coordinates are validated via {@link #setGridPos(GridPos)}.
     * * @param gridPos The initial {@link GridPos} where this modifier will be 
     * placed on the level board.
     */
    public Modifier(GridPos gridPos) {
        this.isDisabled = false;
        this.gridPos = new GridPos();
        setGridPos(gridPos);
    }

    // --- Core Logic & Lifecycle ---

    /**
     * Executes the specific modification logic for this tile.
     * Must be implemented by subclasses.
     * @param toModify The card currently interacting with the tile.
     */
    public abstract void modify(Card toModify);

    /**
     * Resets the modifier to its initial level-start state.
     */
    public abstract void reset();

    /**
     * Records a successful card modification in the current game level.
     * This is used for animation triggers and level state tracking.
     */
    public void onSuccess() {
        if (GameLevel.getInstance() == null) return;
        GameLevel.getInstance().successfullyModified.add(this);
    }

    // --- Material Interaction Guards ---

    /**
     * Checks if the modifier is disabled or should be disabled by the card.
     * <p>
     * <b>Corruption Mechanic:</b> If a {@link Material#CORRUPTED} card enters, 
     * it permanently disables this modifier and transforms itself into 
     * {@link Material#PLASTIC}.
     * @param card The incoming card.
     * @return {@code true} if the modifier is currently disabled.
     */
    public boolean checkSetDisable(Card card) {
        if (card == null) return isDisabled();
        if (isDisabled()) return true;
        
        if (card.getMaterial() == Material.CORRUPTED) {
            setDisabled(true);
            card.setMaterial(Material.PLASTIC);
        }
        return isDisabled();
    }

    /**
     * Manages the durability of cards (specifically Glass).
     * <p>
     * Every time a card interacts with a modifier, its health decreases by 1. 
     * If health reaches 0, the card is removed from the game. 
     * Note: A health of -1 represents infinite durability.
     * @param card The card to process.
     * @return {@code true} if the card was destroyed (shattered).
     */
    public boolean checkDestroyGlass(Card card) { 
        if (card == null) return false;
        card.setHealth(card.getHealth() - 1);
        if (card.getHealth() == 0) { 
            GameLevel.getInstance().removeCard(card);
            return true;
        }
        return false;
    }

    // --- Getters, Setters, & UI ---

    /**
     * Checks the current operational status of the modifier.
     * @return {@code true} if the modifier is disabled (e.g., by Corruption), 
     * {@code false} otherwise.
     */
    public boolean isDisabled() { return isDisabled; }

    /**
     * Explicitly sets the operational status of the modifier.
     * @param disabled The new disabled state.
     */
    public void setDisabled(boolean disabled) { isDisabled = disabled; }

    /**
     * Retrieves the current grid coordinates of this modifier.
     * @return A {@link GridPos} object representing the tile location.
     */
    @Override
    public GridPos getGridPos() { return gridPos; }

    /**
     * Updates the modifier's position on the board.
     * <p>
     * The input coordinates are clamped between 0 and the maximum width/height 
     * defined in {@link GameLevel} to prevent out-of-bounds placement.
     * @param point The target {@link GridPos} for the modifier.
     */
    @Override
    public void setGridPos(GridPos point) {
        this.gridPos.setX(Math.clamp(point.getX(), 0, GameLevel.MAX_WIDTH));
        this.gridPos.setY(Math.clamp(point.getY(), 0, GameLevel.MAX_HEIGHT));
    }

    /**
     * Returns the primary tooltip explaining the modifier's function.
     * @return A {@link Tooltip} object with descriptive text and theme colors.
     */
    @Override
    public Tooltip getTooltip() {
        return getModifierTooltip();
    }

    /**
     * Provides a static, generic tooltip for the abstract Modifier type.
     * Used primarily for UI elements that reference the category as a whole.
     * @return A default {@link Tooltip} for a generic machine tile.
     */
    public static Tooltip getModifierTooltip() { 
        return new Tooltip("Modifier", Color.FORESTGREEN, "A special machine that can modify a card that occupies the same tile as it");
    }

    /**
     * Provides a warning tooltip if the modifier has been sabotaged or disabled.
     * @return A red "Disabled" {@link Tooltip} if {@link #isDisabled()} is true, 
     * otherwise {@code null}.
     */
    public Tooltip getDisabledTooltip() { 
        return isDisabled() ? new Tooltip("Disabled", Color.RED, "This modifier will not work anymore.") : null;
    }

    /**
     * Returns the class name of the specific implementation.
     * @return The simple name of the subclass (e.g., "Adder", "Vaporizer").
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}