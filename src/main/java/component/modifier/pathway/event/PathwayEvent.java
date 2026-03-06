package component.modifier.pathway.event;

import component.card.Card;
import event.Event;
import util.GridPos;

/**
 * The {@code PathwayEvent} is the base class for all tile-related occurrences 
 * in the game.
 * <p>
 * It encapsulates the spatial context and the card entity involved in an event, 
 * allowing listeners (like the UI or GameLevel) to respond to changes at 
 * specific coordinates.
 */
public class PathwayEvent implements Event {
    
    /** The coordinate on the grid where this event originated. */
    private GridPos pos;
    
    /** The card involved in the event. */
    private Card card;

    /**
     * Constructs a pathway event with the necessary board context.
     * @param pos The {@link GridPos} where the event is taking place.
     * @param card The {@link Card} instance associated with the event.
     */
    public PathwayEvent(GridPos pos, Card card) {
        this.pos = pos;
        this.card = card;
    }

    /** * @return The grid position of the tile that triggered this event.
     */
    public GridPos getPos() {
        return pos;
    }

    /** * @return The card being processed by the pathway.
     */
    public Card getCard() {
        return card;
    }
}