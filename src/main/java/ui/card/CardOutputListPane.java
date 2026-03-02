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

public class CardOutputListPane extends VBox {

    private final Label title;
    private final HBox cardList;
    private Runnable unregisterCardExit;
    private Runnable unregisterReset;


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

    public void cleanup() {
        unregisterCardExit.run();
        unregisterReset.run();
    }

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