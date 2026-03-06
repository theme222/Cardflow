package component.modifier.pathway;

import component.card.Card;
import util.CardCount;
import component.modifier.Modifier;

import java.util.List;

/**
 * The {@code Pathway} class provides the foundation for tiles that track 
 * sequential card data, such as {@link Entrance} and {@link Exit}.
 * <p>
 * It maintains a {@code currentIndex} to keep track of how many cards have 
 * interacted with this tile, allowing it to pull the correct card template 
 * from a provided {@link CardCount} list.
 */
public abstract class Pathway extends Modifier {

    /** Tracks the total number of successful card interactions on this tile. */
    protected int currentIndex;

    public Pathway() {
    }

    /**
     * Determines which card template corresponds to the current interaction index.
     * <p>
     * This method iterates through a list of {@link CardCount} objects, 
     * subtracting counts from the {@code currentIndex} until the correct 
     * group is found. 
     * * @param cardCountList A list defining the sequence of cards for the level.
     * @return A new {@link Card} instance based on the current sequence index, 
     * or {@code null} if the index exceeds the total count.
     */
    public Card getCurrentCard(List<CardCount> cardCountList) {
        // Logic: Iteratively subtract group counts from the global index
        int indexLeft = currentIndex;
        for (CardCount cardCount : cardCountList) {
            // A count of < 0 represents an infinite supply of that card type
            if (cardCount.getCount() < 0 || cardCount.getCount() > indexLeft)
                return new Card(cardCount.getCard());
            
            indexLeft -= cardCount.getCount();
        }
        return null;
    }

    /**
     * @return {@code false}. Pathways (Entrances/Exits) are part of the board 
     * infrastructure and generally do not block card movement.
     */
    @Override
    public boolean isBlocking() {
        return false;
    }

    /**
     * Resets the progression index to 0. 
     * Used when restarting a level to ensure the card sequence begins again.
     */
    @Override
    public void reset() {
        currentIndex = 0;
    }
}