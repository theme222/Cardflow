package logic;

import java.lang.reflect.Array;
import java.util.ArrayList;

import component.card.Card;
import component.modifier.pathway.event.CardExitEvent;
import engine.TickEngine;
import event.EventBus;
import logic.event.end.GameWinEvent;
import util.CardCount;

public class GameWin {
    public static final GameWin INSTANCE = new GameWin();

    public void onWin(GameWinEvent event) {
        // Check if the card that exited is the target card and if it exited through the correct exit
        TickEngine.reset();
        // winnnn do whatever here i guess
    }
}
