package component.modifier.changer;

import component.card.Card;
import component.card.Material;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;

/**
 * The {@code MaterialSetter} class is a specialized {@link Setter} that changes 
 * a {@link Card}'s physical {@link Material}.
 * <p>
 * This modifier is powerful because changing a card's material can fundamentally 
 * alter how it interacts with other tiles on the board (e.g., turning a card 
 * into {@code METAL} to make it immune to arithmetic).
 */
public class MaterialSetter extends Setter<Material> {

    /**
     * Constructs a MaterialSetter with the specified target material.
     * @param material The {@link Material} that cards will be converted to.
     */
    public MaterialSetter(Material material) {
        super();
        this.changeType = ChangeType.MATERIAL;
        this.changeValue = material;
    }

    /**
     * Changes the material of the specified card.
     * <p>
     * <b>Side Effect:</b> Calling {@link Card#setMaterial(Material)} also updates 
     * the card's health properties (e.g., setting it to 3 for {@code GLASS} 
     * or -1 for {@code PLASTIC}).
     * @param toModify The {@link Card} whose material property will be overwritten.
     */
    @Override
    public void change(Card toModify) {
        if (toModify != null) {
            toModify.setMaterial(changeValue);
        }
    }

    /**
     * Generates a descriptive tooltip for the Material Setter.
     * <p>
     * Includes a dynamic reference to the target material and shows the 
     * disabled status if it has been neutralized by a corrupted card.
     * @return A {@link Tooltip} detailing the setter's function and target.
     */
    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            "Material Setter",
            Color.DARKCYAN,
            "A ",
            super.getTooltip(), // Inherits Changer tooltip info
            " that changes the card's material to ",
            Tooltip.ref(getChange()),
            isDisabled() ? "This is currently " : null,
            isDisabled() ? getDisabledTooltip() : null
        );
    }
}