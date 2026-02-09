package ui.inventory;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryPane extends VBox {

    private final PlayerInventory inventory;

    // UI elements we need to update
    private final Text titleText;
    private final Text rotationText;
    private final VBox moversList;

    // Keep references so we can update counts efficiently
    private final Map<String, Text> moverCountTexts = new HashMap<>();

    public InventoryPane(PlayerInventory inventory) {
        this.inventory = inventory;

        setPadding(new Insets(12));
        setSpacing(8);

        // ---- title ----
        titleText = new Text("Inventory");
        titleText.getStyleClass().add("text-heading");

        // ---- rotation ----
        rotationText = new Text();
        rotationText.getStyleClass().add("text-body");

        // ---- movers list ----
        moversList = new VBox(4);

        getChildren().addAll(
                titleText,
                rotationText,
                moversList
        );

        buildMoverList();
        updateUI();
    }

    // Build static rows (called once)
    private void buildMoverList() {
        moversList.getChildren().clear();
        moverCountTexts.clear();

        for (String name : inventory.getCurrentAvailableMovers().keySet()) {
            Text moverText = new Text();
            moverText.getStyleClass().add("text-body");

            moverCountTexts.put(name, moverText);
            moversList.getChildren().add(moverText);
        }
    }

    // Call this whenever inventory changes
    public void updateUI() {
        updateRotation();
        updateMovers();
    }

    private void updateRotation() {
        rotationText.setText(
                "Rotation: " + inventory.getCurrentRotation()
        );
    }

    private void updateMovers() {
        String selected = inventory.getCurrentSelection();

        for (Map.Entry<String, Integer> entry :
                inventory.getCurrentAvailableMovers().entrySet()) {

            String name = entry.getKey();
            int count = entry.getValue();

            Text text = moverCountTexts.get(name);

            String countText = (count == -1) ? "∞" : String.valueOf(count);
            text.setText(name + ": " + countText);

            // ---- selection highlight (CSS-based) ----
            text.getStyleClass().removeAll(
                    "text-strong",
                    "text-muted"
            );

            if (name.equals(selected)) {
                text.getStyleClass().add("text-strong");
            } else if (count == 0) {
                text.getStyleClass().add("text-muted");
            }
        }
    }
}
