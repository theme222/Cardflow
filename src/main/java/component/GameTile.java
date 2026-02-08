package component;

import component.card.Card;
import component.modifier.Modifier;
import component.mover.Mover;
import util.GridIndexable;

public class GameTile {

    private Card card;
    private Modifier modifier;
    private Mover mover;

    public final int posX;
    public final int posY;

    // GETTERS & SETTERS //

    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }
    public Modifier getModifier() { return modifier; }
    public void setModifier(Modifier modifier) { this.modifier = modifier; }
    public Mover getMover() { return mover; }
    public void setMover(Mover mover) { this.mover = mover; }

    // GETTERS & SETTERS //

    // If someone has a better way of doing this I will gladly accept the change.
    public GridIndexable getSameClassOnTile(GridIndexable gridIndexable) {
        if (gridIndexable == null) return null;
        else if (gridIndexable instanceof Card) return getCard();
        else if (gridIndexable instanceof Modifier) return getModifier();
        else if (gridIndexable instanceof Mover) return getMover();
        else throw new IllegalArgumentException("Unknown GridIndexable");
    }

    public void setSameClassOnTile(GridIndexable gridIndexable) {
        if (gridIndexable == null) return;
        else if (gridIndexable instanceof Card) setCard((Card) gridIndexable);
        else if (gridIndexable instanceof Modifier) setModifier((Modifier) gridIndexable);
        else if (gridIndexable instanceof Mover) setMover((Mover) gridIndexable);
        else throw new IllegalArgumentException("Unknown GridIndexable");
    }

    public void removeSameClassOnTile(GridIndexable gridIndexable) {
        if (gridIndexable == null) return;
        else if (gridIndexable instanceof Card) setCard(null);
        else if (gridIndexable instanceof Modifier) setModifier(null);
        else if (gridIndexable instanceof Mover) setMover(null);
        else throw new IllegalArgumentException("Unknown GridIndexable");
    }


    public GameTile(Modifier modifier, int posX, int posY) {
        this.modifier = modifier;
        this.posX = posX;
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "{" +
                card +
                ", " + modifier +
                ", " + mover +
                '}';
    }
}
