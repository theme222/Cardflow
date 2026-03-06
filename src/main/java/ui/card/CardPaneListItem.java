package ui.card;

import component.card.Card;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import util.GridPos;

/**
 * A UI component representing a card within a list (e.g., input or output panes).
 * Includes methods for styling based on equivalence comparisons.
 */
public class CardPaneListItem extends StackPane {

    /** The card information displayed by this item. */
    private final Card cardInfo;
    /** The pane used for rendering the card image. */
    private final Pane renderPane;

    /** 
     * Compares the internal card with another card and sets the border color accordingly.
     * Used to indicate if an exited card matches an expected output card.
     * 
     * @param other The card to compare against.
     */
    public void setBorderColorByComparison(Card other) { // specifically used in CardOutputListPane

        if (cardInfo.isEquivalent(other)) setBorderColor("border-success");
        else setBorderColor("border-error");

    }

    /** 
     * Sets the border color style class for this pane.
     * 
     * @param styleClass The CSS class to apply (e.g., "border-success").
     */
    public void setBorderColor(String styleClass) { // success warning error
        getStyleClass().removeAll("border", "border-info", "border-success", "border-warning", "border-error");
        getStyleClass().add(styleClass);
    }

    /**
     * Constructs a new CardPaneListItem with a default border.
     * 
     * @param cardInfo The card to display.
     */
    public CardPaneListItem(Card cardInfo) {
        this(cardInfo, "border");
    }

    /**
     * Constructs a new CardPaneListItem with a specified style class.
     * 
     * @param cardInfo The card to display.
     * @param defaultStyleClass The initial CSS style class.
     */
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
