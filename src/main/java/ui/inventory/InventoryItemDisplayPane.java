package ui.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InventoryItemDisplayPane extends VBox { // Generic object displayer (Maybe pass through string as args and it shows the image + name?)
    private String objectName; // Just to track
    private Label objectNameLabel; // TODO: Future change this to image
    private Label textDescriberLabel; // Right now is used for the amount of objects

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public InventoryItemDisplayPane(String objectName, int objectCount) {
        setPrefSize(100, 100);
        setPadding(new Insets(10));
        objectNameLabel = new Label();
        objectNameLabel.getStyleClass().addAll("text-subheading", "text-strong", "text-wrap");

        textDescriberLabel = new Label();
        textDescriberLabel.getStyleClass().addAll("text-body", "text-strong", "text-wrap");

        updateUI(objectCount);
        this.objectName = objectName;
    }

    public void updateUI(int objectCount) {
        String toDisplay = String.valueOf(objectCount);
        if (objectCount == -1) toDisplay = "∞";

        objectNameLabel.setText(objectName);
        textDescriberLabel.setText(String.valueOf(objectCount));

        textDescriberLabel.getStyleClass().remove("text-warning");
        if (objectCount == 0) textDescriberLabel.getStyleClass().add("text-warning");
    }

}
