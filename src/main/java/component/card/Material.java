package component.card;

import component.modifier.Modifier;
import component.modifier.changer.Adder;
import component.modifier.changer.Changer;
import component.modifier.combinator.Combinator;
import javafx.scene.paint.Color;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.Util;

/**
 * The {@code Material} enum defines the physical properties and behavioral traits 
 * of a {@link Card}.
 * <p>
 * Each material grants the card unique abilities or vulnerabilities when interacting 
 * with various {@link Modifier} tiles on the grid.
 */
public enum Material implements Tippable {
    /** The standard material with no special properties or immunities. */
    PLASTIC,

    /** * A fragile material. Cards made of glass have limited health and will 
     * shatter after a set number of modifier interactions.
     */
    GLASS,

    /** * A conductive but rigid material. Metal cards are immune to arithmetic 
     * {@link Changer} effects.
     */
    METAL,

    /** * A heavy, solid material. Stone cards are unaffected by {@link Combinator} 
     * logic, preventing them from being merged or split.
     */
    STONE,

    /** * A highly reactive material. Rubber cards trigger the effects of any 
     * {@link Modifier} they enter twice in a single pass.
     */
    RUBBER,

    /** * An unstable material. Corrupted cards disable the first modifier they touch 
     * and then transform into standard {@link #PLASTIC}.
     */
    CORRUPTED;

    /**
     * Returns a capitalized string representation of the material name.
     * @return The formatted name (e.g., "Plastic", "Corrupted").
     */
    @Override
    public String toString() {
        return Util.capitalize(this.name());
    }

    /**
     * Generates a descriptive tooltip explaining the unique gameplay mechanics 
     * associated with this material.
     * <p>
     * Utilizes {@link Tooltip#ref} to create dynamic links to related modifiers 
     * or materials within the UI.
     * @return A {@link Tooltip} configured with the material's specific lore and logic.
     */
    @Override
    public Tooltip getTooltip() {
        return switch (this) {
            case PLASTIC -> new Tooltip(
                "Plastic",
                Color.BROWN
            );
            case GLASS -> new Tooltip(
                "Glass",
                Color.BROWN,
                "This material is very fragile. It will break after passing through ",
                Tooltip.ref(3),
                " ",
                Tooltip.ref(Modifier.getModifierTooltip()),
                "s"
            );
            case METAL -> new Tooltip(
                "Metal",
                Color.BROWN,
                "This material will be unaffected by arithmetic ",
                Tooltip.ref(Changer.getModifierTooltip()),
                "s"
            );
            case STONE -> new Tooltip(
                "Stone",
                Color.BROWN,
                "This material will be unaffected by ",
                Tooltip.ref(Combinator.getCombinatorTooltip()),
                "s"
            );
            case RUBBER -> new Tooltip(
                "Rubber",
                Color.BROWN,
                "Extra bouncy! This material will apply effects from ",
                Tooltip.ref(Modifier.getModifierTooltip()),
                " twice"
            );
            case CORRUPTED -> new Tooltip(
                "Corrupted",
                Color.BROWN,
                "This material will disable the first ",
                Tooltip.ref(Modifier.getModifierTooltip()),
                " it enters. It then reverts to a ",
                Tooltip.ref(PLASTIC),
                " card"
            );
        };
    }
}