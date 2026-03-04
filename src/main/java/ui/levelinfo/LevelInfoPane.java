package ui.levelinfo;

import application.view.GameView;
import audio.AudioManager;
import engine.event.PausedEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logic.GameLevel;
import logic.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

import engine.TickEngine;
import engine.event.ModifyEndedEvent;
import engine.event.MovementEndedEvent;
import event.EventBus;
import ui.card.CardInputListPane;
import ui.card.CardOutputListPane;
import ui.tooltip.Tooltip;
import ui.tooltip.TooltipLayer;
import util.Direction;

public class LevelInfoPane extends VBox { // thx chatgpt

    private final PlayerInventory inventory;

    private final Text titleText;
    private final Label rotationLabel;
    private final VBox moversList;
    private final HBox controlPanel;
    private final Label phaseLabel;
    private final CardInputListPane cardInputListPane;
    private final CardOutputListPane cardOutputListPane;

    // Per-mover UI references
    private final Map<String, Button> moverButtons = new HashMap<>();
    private final Map<String, Text> moverCountTexts = new HashMap<>();

    private Runnable unregisterAfterMovement;
    private Runnable unregisterAfterModify;
    private Runnable unregisterAfterPause;

    public LevelInfoPane(TooltipLayer tooltipLayer) {
        inventory = PlayerInventory.getInstance();

        setPadding(new Insets(12));
        setSpacing(10);
        setAlignment(Pos.CENTER);

        titleText = new Text(GameLevel.getInstance().LEVELNAME);
        titleText.getStyleClass().add("text-heading");

        controlPanel = new HBox(10);

        phaseLabel = new Label("Current Phase: Paused   ");

        rotationLabel = new Label();
        rotationLabel.getStyleClass().add("text-body");

        HBox statusContainer = new HBox(10);
        statusContainer.setAlignment(Pos.CENTER);
        statusContainer.getChildren().addAll(phaseLabel, rotationLabel);

        moversList = new VBox(6);

        cardInputListPane = new CardInputListPane("Input Cards", GameLevel.getInstance().INPUT_CARDS, tooltipLayer);

        cardOutputListPane = new CardOutputListPane("Output Cards", GameLevel.getInstance().OUTPUT_CARDS, tooltipLayer);

        getChildren().addAll(
                titleText,
                cardInputListPane,
                cardOutputListPane,
                statusContainer,
                controlPanel,
                moversList
        );

        buildMoverRows(tooltipLayer);
        buildControlPanel();
        updateInventoryUI();

        registerPhaseLabel();
    }

    private void registerPhaseLabel(){
        unregisterAfterMovement = EventBus.register(MovementEndedEvent.class, this::afterMovement);
        unregisterAfterModify = EventBus.register(ModifyEndedEvent.class, this::afterModifying);
        unregisterAfterPause = EventBus.register(PausedEvent.class, this::afterPaused);
    }

    public void cleanup() {
        unregisterAfterMovement.run();
        unregisterAfterModify.run();
        unregisterAfterPause.run();
        cardOutputListPane.cleanup();
    }

    private void afterMovement(MovementEndedEvent event){
        phaseLabel.setText("Current Phase: Moving   ");
    }

    private void afterModifying(ModifyEndedEvent event){
        phaseLabel.setText("Current Phase: Modifying"); // do whatever changes here
    }

    private void afterPaused(PausedEvent event){
        phaseLabel.setText("Current Phase: Paused   "); // do whatever changes here
    }

    private void buildControlPanel() {
        Button playButton = new Button("Play ▶");
        playButton.getStyleClass().add("button-success");
        Button pauseButton = new Button("Pause ǁ");
        pauseButton.getStyleClass().add("button-warning");
        Button stepButton = new Button("Step →");
        stepButton.getStyleClass().add("button-info");
        Button resetButton = new Button("Reset ☕");
        resetButton.getStyleClass().add("button-error");

        playButton.setOnAction(e -> {
            AudioManager.playSoundEffect("button-click");
            TickEngine.play();
        });
        pauseButton.setOnAction(e -> {
            AudioManager.playSoundEffect("button-click");
            TickEngine.pause();
        });
        stepButton.setOnAction(e -> {
            AudioManager.playSoundEffect("button-click");
            TickEngine.step();
        });
        resetButton.setOnAction(e -> {
            AudioManager.playSoundEffect("button-click");
            TickEngine.reset();
        });

        controlPanel.getChildren().addAll(playButton, pauseButton, stepButton, resetButton);
    }

    // Build static UI once
    private void buildMoverRows(TooltipLayer tooltipLayer) {
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
                AudioManager.playSoundEffect("button-click");
                updateInventoryUI();
            });

            tooltipLayer.bind(button,
                Tooltip.getContainerFor(PlayerInventory.getMoverObjectByName(name, PlayerInventory.getInstance().getCurrentRotation()))
            );

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
    public void updateInventoryUI() {
        updateRotation();
        updateMovers();
    }

    private void updateRotation() {
        rotationLabel.setText(
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
                    "button-primary",
                    "button-error",
                    "text-muted"
            );
            countText.getStyleClass().removeAll(
                    "text-error",
                    "text-muted"
            );

            // ---- state styling ----
            if (name.equals(selected)) {
                button.getStyleClass().add("button-primary");
            } else if (!available) {
                button.getStyleClass().addAll("text-muted", "button-error");
                countText.getStyleClass().addAll("text-muted", "text-error");
            }
        }
    }
}
