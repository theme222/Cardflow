package ui.inventory;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.PlayerInventory;

public class InventoryUIPane extends VBox {

    InventoryItemDisplayPane[] displayPanes;

    public InventoryUIPane() {
        setSpacing(10);
        setPadding(new Insets(15));

        PlayerInventory inventory = PlayerInventory.getInstance();

        Label title = new Label("Inventory");
        title.getStyleClass().addAll("text-heading", "text-center");
        getChildren().add(title);

        HBox inventoryPane = new HBox();
        inventoryPane.setSpacing(10);

        displayPanes = new InventoryItemDisplayPane[inventory.getCurrentAvailableMovers().size()];
        int index = 0;

        for (String moverNames: inventory.getCurrentAvailableMovers().keySet()) {
            InventoryItemDisplayPane display = new InventoryItemDisplayPane(
                    moverNames,
                    inventory.getCurrentAvailableMovers().get(moverNames)
            );
            displayPanes[index] = display;
            inventoryPane.getChildren().add(display);
            index++;
        }

    }

    public void updateUI() {
        PlayerInventory inventory = PlayerInventory.getInstance();

        for (InventoryItemDisplayPane display: displayPanes) {
            int newCount = inventory.getCurrentAvailableMovers().get(display.getObjectName());
            display.updateUI(newCount);
        }
    }
}
