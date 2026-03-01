package ui.card;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ui.tooltip.Tooltip;
import ui.tooltip.TooltipLayer;
import util.CardCount;
import util.GridPos;

import java.util.List;

public class CardInputListPane extends VBox {

    private final Label title;
    private final HBox cardList;

    public CardInputListPane(String name, List<CardCount> cardCounts, TooltipLayer tooltipLayer) {
        title = new Label(name);
        title.setPadding(new Insets(5));
        title.getStyleClass().add("text-subheading");

        cardList = new HBox();
        cardList.setSpacing(10);
        cardList.setPadding(new Insets(10));

        for (CardCount cardCount : cardCounts) {
            for (int i = 0; i < cardCount.getCount(); i++) {
                CardPaneListItem cardPane = new CardPaneListItem(cardCount.getCard());
                tooltipLayer.bind(cardPane, Tooltip.getContainerFor(cardCount.getCard()));
                cardList.getChildren().add(cardPane);
            }
        }

        getChildren().addAll(title, cardList);
        setBorder(Border.stroke(Color.BLACK));
    }

}
