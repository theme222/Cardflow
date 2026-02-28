package ui.tooltip;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TooltipPane extends VBox {

    public TooltipPane(Tooltip tooltip) {
        getStyleClass().add("tooltip");

        Label titleLabel = new Label(tooltip.getTitle());
        titleLabel.getStyleClass().add("text-subheading");
        titleLabel.setTextFill(tooltip.getTitleColor());

        getChildren().addAll(titleLabel, tooltip.getDescription());
        setAlignment(Pos.TOP_CENTER);
        setSpacing(10);
        setPadding(new Insets(8));
        setBackground(Background.fill(Color.RED));
        setMaxWidth(300);
    }

}
