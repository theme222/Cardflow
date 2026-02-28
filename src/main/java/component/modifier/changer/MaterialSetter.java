package component.modifier.changer;

import component.card.Card;
import component.card.Material;
import logic.GameLevel;

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
}
