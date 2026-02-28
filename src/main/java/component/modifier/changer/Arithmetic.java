package component.modifier.changer;

import component.card.Card;
import component.card.Material;

public abstract class Arithmetic extends Changer<Integer> {
    // Used for resolving the image

    @Override
    public void modify(Card card) {
        if (card == null) return;
        if (card.getMaterial() == Material.METAL) return;

        super.modify(card);
    }
}
