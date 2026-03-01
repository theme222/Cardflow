package component.modifier.changer;

import component.card.Card;
import component.card.Material;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;

public class MaterialSetter extends Setter<Material> {

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setMaterial(changeValue);
    }

    public MaterialSetter(Material material) {
        super();
        this.changeType = ChangeType.MATERIAL;
        this.changeValue = material;
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
                "Material Setter",
                Color.DARKGRAY,
                "A ",
                super.getTooltip(), // changer
                " that changes the card's material to ",
                Tooltip.ref(getChange())
        );
    }
}
