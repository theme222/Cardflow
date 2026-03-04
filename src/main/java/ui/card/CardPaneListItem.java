package ui.card;

import component.card.Card;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import util.GridPos;

public class CardPaneListItem extends StackPane {

    private final Card cardInfo;
    private final Pane renderPane;

    public void setBorderColorByComparison(Card other) { // specifically used in CardOutputListPane

        System.out.println("setBorderColorByComparison" + other.toString());
        if (cardInfo.isEquivalent(other)) setBorderColor("border-success");
        else setBorderColor("border-error");

    }

    public void setBorderColor(String styleClass) { // success warning error
        getStyleClass().removeAll("border", "border-info", "border-success", "border-warning", "border-error");
        getStyleClass().add(styleClass);
    }

    public CardPaneListItem(Card cardInfo) {
        this(cardInfo, "border");
    }

    public CardPaneListItem(Card cardInfo, String defaultStyleClass) {
        this.cardInfo = new Card(cardInfo); // copy
        Pane renderPane = new Pane();
        this.renderPane = renderPane;

        CardRenderer.INSTANCE.render(this.cardInfo, this.renderPane, new GridPos(), false, false, false);

        getChildren().add(renderPane);
        setAlignment(Pos.CENTER);
        setBorderColor(defaultStyleClass);
    }

}
