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

/**
 * A UI pane that displays a list of cards available as input for a level.
 */
public class CardInputListPane extends VBox {

    /** The title label for the input list. */
    private final Label title;
    /** Container for the card items. */
    private final HBox cardList;

    /**
     * Constructs a new CardInputListPane.
     * 
     * @param name The display name (title) for the list.
     * @param cardCounts The input card counts.
     * @param tooltipLayer The tooltip manager.
     */
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
