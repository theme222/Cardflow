package component.modifier.combinator;

import component.card.Card;
import component.modifier.Modifier;

public abstract class Combinator extends Modifier {
    // Used for rendering

    @Override
    public void reset() {this.setDisabled(false);}
}
