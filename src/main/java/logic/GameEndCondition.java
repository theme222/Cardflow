package logic;

import java.lang.reflect.Array;
import java.util.ArrayList;

import component.card.Card;
import component.modifier.pathway.event.CardExitEvent;
import event.EventBus;
import logic.event.end.GameWinEvent;
import util.CardCount;

/**
 * Handles checking for level completion conditions.
 */
public class GameEndCondition {
    /**
     * The singleton instance of GameEndCondition.
     */
    public static final GameEndCondition INSTANCE = new GameEndCondition();

    /** 
     * Checks if the win condition has been met based on a card exit event.
     * A win is triggered when the total number of exited cards matches the required output count,
     * and all those cards exited through the correct endpoints.
     * 
     * @param event The {@link CardExitEvent} detailing the current exit statistics.
     */
    public void checkWinCondition(CardExitEvent event) {
        // Check if the card that exited is the target card and if it exited through the correct exit
        int totalExpectedCards = GameLevel.getInstance().OUTPUT_CARDS.stream().mapToInt((cc)->cc.getCount()).sum();
        
        if (event.getCountTotalExits() == totalExpectedCards && event.getCountCorrectExits() == totalExpectedCards) {
            EventBus.emit(new GameWinEvent());
        }
    }
}
