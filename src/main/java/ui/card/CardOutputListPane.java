package ui.card;

import component.card.Card;
import component.modifier.pathway.event.CardExitEvent;
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

        EventBus.register(CardExitEvent.class, this::updateUI); // I'm pretty sure if you reallllllly tried this could cause a memory leak but honestly idc its easier than rerouting everything or creating an on free
    }

    public void updateUI(CardExitEvent cardExitEvent) {
        List<Card> exitedCardList = GameLevel.getInstance().exitedCardsList;
        for (int index = 0; index < exitedCardList.size(); index++) {
            if (cardList.getChildren().size() <= index) continue; // Any extra input is ignored ?
            ((CardPaneListItem)cardList.getChildren().get(index)).setBorderColorByComparison(exitedCardList.get(index));
        }
    }



}