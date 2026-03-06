package ui.card;

import component.card.Card;
import component.modifier.pathway.event.CardExitEvent;
import engine.event.ResetEvent;
import event.Event;
import event.EventBus;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import logic.GameLevel;
import ui.tooltip.Tooltip;
import ui.tooltip.TooltipLayer;
import util.CardCount;

import java.util.List;

/**
 * A UI pane that displays a list of cards required for output to complete a level.
 * It updates dynamically as cards exit the level, highlighting correct or incorrect matches.
 */
public class CardOutputListPane extends VBox {

    /** The title label for the output list. */
    private final Label title;
    /** Container for the card items. */
    private final HBox cardList;
    /** Runnable to unregister the card exit listener. */
    private Runnable unregisterCardExit;
    /** Runnable to unregister the reset event listener. */
    private Runnable unregisterReset;


    /**
     * Constructs a new CardOutputListPane.
     * 
     * @param name The display name (title) for the list.
     * @param cardCounts The target card counts.
     * @param tooltipLayer The tooltip manager for binding tooltips to cards.
     */
    public CardOutputListPane(String name, List<CardCount> cardCounts, TooltipLayer tooltipLayer) {
        title = new Label(name);
        title.setPadding(new Insets(5));
        title.getStyleClass().add("text-subheading");

        cardList = new HBox();
        cardList.setSpacing(10);
        cardList.setPadding(new Insets(10));

        for (CardCount cardCount : cardCounts) {
            for (int i = 0; i < cardCount.getCount(); i++) {
                CardPaneListItem cardPane = new CardPaneListItem(cardCount.getCard(), "border-warning");
                tooltipLayer.bind(cardPane, Tooltip.getContainerFor(cardCount.getCard()));
                cardList.getChildren().add(cardPane);
            }
        }

        getChildren().addAll(title, cardList);
        setBorder(Border.stroke(Color.BLACK));

        unregisterCardExit = EventBus.register(CardExitEvent.class, this::updateUI);
        unregisterReset = EventBus.register(ResetEvent.class, this::updateUI);
    }

    /**
     * Unregisters event listeners to prevent memory leaks.
     */
    public void cleanup() {
        unregisterCardExit.run();
        unregisterReset.run();
    }

    /** 
     * Updates the UI based on the current set of exited cards in the game level.
     * 
     * @param e The event that triggered the update (CardExitEvent or ResetEvent).
     */
    public void updateUI(Event e) {
        List<Card> exitedCardList = GameLevel.getInstance().exitedCardsList;
        int index;
        for (index = 0; index < exitedCardList.size(); index++) {
            if (cardList.getChildren().size() <= index) continue; // Any extra input is ignored ?
            ((CardPaneListItem)cardList.getChildren().get(index)).setBorderColorByComparison(exitedCardList.get(index));
        }

        // Force set the others to warning
        for (; index < cardList.getChildren().size(); index++) {
            ((CardPaneListItem)cardList.getChildren().get(index)).setBorderColor("border-warning");
        }

    }



}
