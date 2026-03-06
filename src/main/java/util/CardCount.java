package util;

import component.card.Card;

/**
 * The {@code CardCount} class is a simple wrapper that associates a 
 * {@link Card} template with a quantity.
 * <p>
 * It is primarily used to define level requirements (input and output targets) 
 * and to track the remaining cards in a source or destination.
 */
public class CardCount {
    private Card card;
    private int count;

    /** 
     * Constructs a new {@code CardCount}.
     * @param card The template card.
     * @param count The number of cards (use -1 for infinite).
     */
    public CardCount(Card card, int count) {
        setCard(card);
        setCount(count);
    }

    /** @return The template {@link Card}. */
    public Card getCard() {
        return card;
    }

    /** @param card The new template {@link Card} to wrap. */
    public void setCard(Card card) {
        if (card == null) card = new Card();
        this.card = card;
    }

    /** @return The current quantity. */
    public int getCount() {
        return count;
    }

    /** 
     * Sets the quantity, ensuring it remains within a valid range.
     * @param count The new count (-1 for infinity, otherwise non-negative).
     */
    public void setCount(int count) {
        this.count = Math.clamp(count, -1, Integer.MAX_VALUE); // -1 is infinity
    }
}
