package logic;

import java.lang.reflect.Array;
import java.util.ArrayList;

import component.card.Card;
import component.modifier.pathway.event.CardExitEvent;
import event.EventBus;
import logic.event.end.GameWinEvent;
import util.CardCount;

public class GameEndCondition {
    public static final GameEndCondition INSTANCE = new GameEndCondition();

    /** 
     * @param event
     */
    public void checkWinCondition(CardExitEvent event) {
        // Check if the card that exited is the target card and if it exited through the correct exit
        int totalExpectedCards = GameLevel.getInstance().OUTPUT_CARDS.stream().mapToInt((cc)->cc.getCount()).sum();
        
        if (event.getCountTotalExits() == totalExpectedCards && event.getCountCorrectExits() == totalExpectedCards) {
            EventBus.emit(new GameWinEvent());
        }
    }
}
