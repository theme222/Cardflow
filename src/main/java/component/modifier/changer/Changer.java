package component.modifier.changer;

import component.card.Card;
import component.card.Material;
import component.modifier.Modifier;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

/**
 * The {@code Changer} class is a generic base for modifiers that alter a specific property of a {@link Card}.
 * <p>
 * It handles the core lifecycle of a modification, including material-specific interactions 
 * (Glass breakage, Rubber double-triggering, and Corrupted disabling).
 * * @param <T> The type of the property being changed (e.g., Integer for values, Suit for card suits).
 */
abstract public class Changer<T> extends Modifier { 

    /** The target value or state that will be applied to the card. */
    protected T changeValue;

    /** The category of property this changer targets. */
    protected ChangeType changeType;

    /**
     * Defines the categories of card properties that can be modified.
     */
    public enum ChangeType {
        /** Targets the numerical value of the card. */
        NUMBER,
        /** Targets the card's {@link component.card.Suit}. */
        SUIT,
        /** Targets the card's {@link component.card.Material}. */
        MATERIAL
    }

    public Changer() {}

    /** @return The value/state this modifier intends to apply. */
    public T getChange() { return changeValue; }

    /** @param changeValue The value/state to be assigned to this modifier. */
    public void setChange(T changeValue) { this.changeValue = changeValue; }

    /** @return The category of change this class performs. */
    public ChangeType getChangeType() { return changeType; }

    /**
     * Performs the specific logic of the modification.
     * <p>
     * Subclasses (like {@link Adder} or {@link SuitSetter}) must implement this to 
     * define exactly how the card's internal state is updated.
     * @param toModify The card to be altered.
     */
    public abstract void change(Card toModify);

    /**
     * Returns the blocking status of the modifier tile.
     * @return Always {@code false}, as cards must be able to enter the tile to be modified.
     */
    @Override
    public boolean isBlocking() { return false; }

    /**
     * The primary entry point for a card entering a modification tile.
     * <p>
     * The logic follows a strict priority:
     * <ol>
     * <li><b>Disable Check:</b> If the card is {@link Material#CORRUPTED}, it may disable this modifier.</li>
     * <li><b>Glass Check:</b> If the card is {@link Material#GLASS}, its health is reduced; if it hits 0, it shatters and logic stops.</li>
     * <li><b>Rubber Check:</b> If the card is {@link Material#RUBBER}, the {@link #change(Card)} method is called twice.</li>
     * <li><b>Standard Execution:</b> The {@link #change(Card)} method is called.</li>
     * </ol>
     * @param toModify The card passing through the tile.
     */
    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;
        if (checkDestroyGlass(toModify)) return;
        if (toModify == null) return;
        
        // Double application for Rubber material
        if (toModify.getMaterial() == Material.RUBBER) change(toModify);
        
        change(toModify);
        onSuccess();
    }

    /**
     * Resets the modifier to its default state, specifically re-enabling it.
     */
    @Override
    public void reset() { this.setDisabled(false); }

    /** @return A specialized tooltip describing the Changer's role. */
    @Override
    public Tooltip getTooltip() { return getChangerTooltip(); }

    /**
     * Static helper to provide a consistent tooltip for the abstract Changer class.
     * @return A {@link Tooltip} explaining that this component changes card properties.
     */
    public static Tooltip getChangerTooltip() { 
        return new Tooltip(
                "Changer",
                Color.BLUEVIOLET,
                "A ",
                getModifierTooltip(),
                " that changes a property of a card."
        );
    }
}