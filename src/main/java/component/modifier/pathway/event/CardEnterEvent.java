package component.modifier.pathway.event;

import component.card.Card;
import util.GridPos;

/**
 * The {@code CardEnterEvent} is fired whenever a {@link Card} moves 
 * into a specific grid coordinate.
 * <p>
 * This event acts as the "handshake" between the game logic and the 
 * physical tile. It provides the necessary context (position and card identity) 
 * for modifiers to begin their execution sequence.
 */
public class CardEnterEvent extends PathwayEvent {

    /**
     * Constructs a new enter event.
     * @param pos The {@link GridPos} (column, row) that the card is entering.
     * @param card The {@link Card} instance that has moved onto the tile.
     */
    public CardEnterEvent(GridPos pos, Card card) {
        super(pos, card);
    }
    
}