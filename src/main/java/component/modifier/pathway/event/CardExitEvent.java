package component.modifier.pathway.event;

import java.util.ArrayList;
import component.card.Card;
import util.GridPos;

/**
 * The {@code CardExitEvent} is fired when a {@link Card} departs a tile, 
 * usually signifying it has reached a goal or terminal point in the path.
 * <p>
 * This event is vital for the {@link logic.GameLevel} to determine if win conditions 
 * are met, as it carries the running totals of correct and total exits.
 */
public class CardExitEvent extends PathwayEvent {
    
    /** Records the cards that have successfully passed through this exit. */
    private ArrayList<Card> exitedCards;
    
    /** The total number of cards that matched the required suit/value upon exit. */
    private int countCorrectExits;
    
    /** The total number of cards that have reached an exit tile, regardless of correctness. */
    private int countTotalExits;

    /**
     * Constructs a new exit event with current level statistics.
     * @param pos The {@link GridPos} of the exit tile.
     * @param card The {@link Card} that is exiting.
     * @param countCorrectExits Cumulative successful matches.
     * @param countTotalExits Cumulative total card departures.
     */
    public CardExitEvent(GridPos pos, Card card, int countCorrectExits, int countTotalExits) {
        super(pos, card);
        this.countCorrectExits = countCorrectExits;
        this.countTotalExits = countTotalExits;
    }

    /** * @return The number of cards that satisfied the level's objective goals.
     */
    public int getCountCorrectExits() {
        return countCorrectExits;
    }

    /** * @return The total volume of cards that have left the board via this exit.
     */
    public int getCountTotalExits() {
        return countTotalExits;
    }
}