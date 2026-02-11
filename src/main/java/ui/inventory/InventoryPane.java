package ui.inventory;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class InventoryPane extends VBox { // thx chatgpt

    private final PlayerInventory inventory;

    private final Text titleText;
    private final Text rotationText;
    private final VBox moversList;

    // Per-mover UI references
    private final Map<String, Button> moverButtons = new HashMap<>();
    private final Map<String, Text> moverCountTexts = new HashMap<>();

    public InventoryPane(PlayerInventory inventory) {
        this.inventory = inventory;

        setPadding(new Insets(12));
        setSpacing(10);

        titleText = new Text("Inventory");
        titleText.getStyleClass().add("text-heading");

        rotationText = new Text();
        rotationText.getStyleClass().add("text-body");

        moversList = new VBox(6);

        getChildren().addAll(
                titleText,
                rotationText,
                moversList
        );

        buildMoverRows();
        updateUI();
    }

    // Build static UI once
    private void buildMoverRows() {
        moversList.getChildren().clear();
        moverButtons.clear();
        moverCountTexts.clear();

        for (String name : inventory.getCurrentAvailableMovers().keySet()) {

            // --- button ---
            Button button = new Button(name);
            button.setFocusTraversable(false);
            button.getStyleClass().add("text-body");

            button.setOnAction(e -> {
                inventory.setCurrentSelection(name);
                updateUI();
            });

            // --- count text ---
            Text countText = new Text();
            countText.getStyleClass().add("text-body");

            // --- row ---
            HBox row = new HBox(8, button, countText);
            row.setAlignment(Pos.CENTER_LEFT);

            moverButtons.put(name, button);
            moverCountTexts.put(name, countText);

            moversList.getChildren().add(row);
        }
    }

    // Refresh all dynamic state
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

        for (var entry : inventory.getCurrentAvailableMovers().entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();

            Button button = moverButtons.get(name);
            Text countText = moverCountTexts.get(name);

            // ---- count display ----
            String countStr = (count == -1) ? "∞" : String.valueOf(count);
            countText.setText("x " + countStr);

            // ---- enable / disable ----
            boolean available = (count != 0);
            button.setDisable(!available);

            // ---- style cleanup ----
            button.getStyleClass().removeAll(
                    "text-strong",
                    "text-muted"
            );
            countText.getStyleClass().remove("text-muted");

            // ---- state styling ----
            if (name.equals(selected)) {
                button.getStyleClass().add("text-strong");
            } else if (!available) {
                button.getStyleClass().add("text-muted");
                countText.getStyleClass().add("text-muted");
            }
        }
    }
}
