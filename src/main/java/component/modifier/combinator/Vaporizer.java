package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;
import logic.GameLevel;

public class Vaporizer extends Combinator {
    public Vaporizer() {}

    @Override
    public void modify(Card toModify) {
        if (checkSetDisable(toModify)) return;

        if (toModify != null) GameLevel.getInstance().removeCard(toModify);
    }

    @Override
    public boolean isBlocking() {
        return false;
    }
}
