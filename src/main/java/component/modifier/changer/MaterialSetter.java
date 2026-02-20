package component.modifier.changer;

import component.card.Card;
import logic.GameLevel;

public class MaterialSetter extends Changer<Card.Material> {

    @Override
    public void change(Card toModify) {
        if (toModify != null) toModify.setMaterial(changeValue);
    }

    public MaterialSetter(Card.Material material) {
        super();
        this.changeType = ChangeType.MATERIAL;
        this.changeValue = material;
    }
}
