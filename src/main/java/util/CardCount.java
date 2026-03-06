package util;

import component.card.Card;

public class CardCount { // Card wrapper for entrance and exit, input and output cards.
    private Card card;
    private int count;

    public CardCount(Card card, int count) {
        setCard(card);
        setCount(count);
    }

    /** 
     * @return Card
     */
    public Card getCard() {
        return card;
    }

    /** 
     * @param card
     */
    public void setCard(Card card) {
        if (card == null) card = new Card();
        this.card = card;
    }

    /** 
     * @return int
     */
    public int getCount() {
        return count;
    }

    /** 
     * @param count
     */
    public void setCount(int count) {
        this.count = Math.clamp(count, -1, Integer.MAX_VALUE); // -1 is infinity
    }
}
