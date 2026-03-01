package component;

import component.card.Card;
import component.modifier.Modifier;
import component.mover.Mover;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tippable;
import ui.tooltip.Tooltip;
import util.GridIndexable;
import util.GridPos;

import java.util.ArrayList;
import java.util.function.Supplier;

public class GameTile implements Tippable {

    private Card card;
    private Modifier modifier;
    private Mover mover;

    public final int posX;
    public final int posY;

    public GameTile(Modifier modifier, int posX, int posY) {
        this.modifier = modifier;
        this.posX = posX;
        this.posY = posY;
    }

    public Card getCard() { return card; }
    public void setCard(Card card) { this.card = card; }

    public Modifier getModifier() { return modifier; }
    public void setModifier(Modifier modifier) { this.modifier = modifier; }

    public Mover getMover() { return mover; }
    public void setMover(Mover mover) { this.mover = mover; }

    public GridPos getGridPos() {
        return new GridPos(posX, posY);
    }

    public GridIndexable getSameClassOnTile(GridIndexable g) {
        if (g == null) return null;
        if (g instanceof Card) return card;
        if (g instanceof Modifier) return modifier;
        if (g instanceof Mover) return mover;
        throw new IllegalArgumentException("Unknown GridIndexable");
    }

    public void setSameClassOnTile(GridIndexable g) {
        if (g instanceof Card) card = (Card) g;
        else if (g instanceof Modifier) modifier = (Modifier) g;
        else if (g instanceof Mover) mover = (Mover) g;
        else if (g != null) throw new IllegalArgumentException("Unknown GridIndexable");
    }

    public void removeSameClassOnTile(GridIndexable g) {
        if (g instanceof Card) card = null;
        else if (g instanceof Modifier) modifier = null;
        else if (g instanceof Mover) mover = null;
        else if (g != null) throw new IllegalArgumentException("Unknown GridIndexable");
    }

    @Override
    public String toString() {
        return "{" + card + ", " + modifier + ", " + mover + '}';
    }

    @Override
    public Tooltip getTooltip() {
        return new Tooltip(
            null,
            Color.BLACK,
            Tooltip.ref(getCard()),
            Tooltip.ref(getModifier()),
            Tooltip.ref(getMover())
        );
    }
}
