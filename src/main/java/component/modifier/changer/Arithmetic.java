package component.modifier.changer;

import component.card.Card;

public abstract class Arithmetic extends Changer<Integer> {
    // Used for resolving the image

    @Override
    public void modify(Card card) {
        if (card == null) return;
        if (card.getMaterial() == Card.Material.METAL) return;
        super.modify(card);
    }
}
