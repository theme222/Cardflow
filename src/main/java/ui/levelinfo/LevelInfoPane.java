package ui.levelinfo;

import application.controller.PlacementController;
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

public class LevelInfoPane extends VBox {

    private enum GamePhase {
        MOVING("Current Phase: Moving   "),
        MODIFYING("Current Phase: Modifying"),
        PAUSED("Current Phase: Paused   ");

        private final String displayText;

        GamePhase(String displayText) {
            this.displayText = displayText;
        }

        public String getDisplayText() {
            return displayText;
        }
    }

    private final PlayerInventory inventory;

    private Text titleText;
    private Label rotationLabel;
    private VBox moversList;
    private HBox controlPanel;
    private Label phaseLabel;
    private CardInputListPane cardInputListPane;
    private CardOutputListPane cardOutputListPane;

    private final Map<String, MoverRowUI> moverRows = new HashMap<>();

    private Runnable unregisterAfterMovement;
    private Runnable unregisterAfterModify;
    private Runnable unregisterAfterPause;

    public LevelInfoPane(TooltipLayer tooltipLayer) {
        inventory = PlayerInventory.getInstance();

        initializeStyle();
        initializeComponents(tooltipLayer);
        buildLayout(tooltipLayer);
        initializeContent(tooltipLayer);
    }

    private void initializeStyle() {
        setPadding(new Insets(12));
        setSpacing(10);
        setAlignment(Pos.CENTER);
    }

    private void initializeComponents(TooltipLayer tooltipLayer) {
        titleText = createTitleText();
        rotationLabel = createRotationLabel();
        phaseLabel = createPhaseLabel();
        controlPanel = new HBox(10);
        moversList = new VBox(6);
        cardInputListPane = new CardInputListPane("Input Cards", GameLevel.getInstance().INPUT_CARDS, tooltipLayer);
        cardOutputListPane = new CardOutputListPane("Output Cards", GameLevel.getInstance().OUTPUT_CARDS, tooltipLayer);
    }

    private Text createTitleText() {
        Text text = new Text(GameLevel.getInstance().LEVELNAME);
        text.getStyleClass().add("text-heading");
        return text;
    }

    private Label createRotationLabel() {
        Label label = new Label();
        label.getStyleClass().add("text-body");
        return label;
    }

    private Label createPhaseLabel() {
        return new Label(GamePhase.PAUSED.getDisplayText());
    }

    private void buildLayout(TooltipLayer tooltipLayer) {
        HBox statusContainer = createStatusContainer();
        getChildren().addAll(
                titleText,
                cardInputListPane,
                cardOutputListPane,
                statusContainer,
                controlPanel,
                moversList
        );
    }

    private void initializeContent(TooltipLayer tooltipLayer) {
        buildMoverRows(tooltipLayer);
        buildControlPanel();
        updateInventoryUI();
        registerEventHandlers();
    }

    private HBox createStatusContainer() {
        HBox statusContainer = new HBox(10);
        statusContainer.setAlignment(Pos.CENTER);
        statusContainer.getChildren().addAll(phaseLabel, rotationLabel);
        return statusContainer;
    }

    private void registerEventHandlers() {
        unregisterAfterMovement = EventBus.register(MovementEndedEvent.class, e -> updatePhase(GamePhase.MOVING));
        unregisterAfterModify = EventBus.register(ModifyEndedEvent.class, e -> updatePhase(GamePhase.MODIFYING));
        unregisterAfterPause = EventBus.register(PausedEvent.class, e -> updatePhase(GamePhase.PAUSED));
    }

    private void updatePhase(GamePhase phase) {
        phaseLabel.setText(phase.getDisplayText());
    }

    public void cleanup() {
        unregisterAfterMovement.run();
        unregisterAfterModify.run();
        unregisterAfterPause.run();
        cardOutputListPane.cleanup();
    }

    private void buildControlPanel() {
        controlPanel.getChildren().addAll(
                createButton("Play ▶", "button-success", TickEngine::play),
                createButton("Pause ǁ", "button-warning", TickEngine::pause),
                createButton("Step →", "button-info", TickEngine::step),
                createButton("Reset ☕", "button-error", TickEngine::reset)
        );
    }

    private Button createButton(String text, String styleClass, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setOnAction(e -> {
            AudioManager.playSoundEffect("button-click");
            action.run();
        });
        return button;
    }

    private void buildMoverRows(TooltipLayer tooltipLayer) {
        moversList.getChildren().clear();
        moverRows.clear();

        for (String name : inventory.getCurrentAvailableMovers().keySet()) {
            MoverRowUI rowUI = createMoverRow(name, tooltipLayer);
            moverRows.put(name, rowUI);
            moversList.getChildren().add(rowUI.getContainer());
        }
    }

    private MoverRowUI createMoverRow(String name, TooltipLayer tooltipLayer) {
        Button button = new Button(name);
        button.setFocusTraversable(false);
        button.getStyleClass().add("text-body");
        button.setOnAction(e -> {
            inventory.setCurrentSelection(name);
            AudioManager.playSoundEffect("button-click");
            updateInventoryUI();
        });

            tooltipLayer.bind(button,
                Tooltip.getContainerFor(PlayerInventory.getMoverObjectByName(name, Direction.UP))
            );

        Text countText = new Text();
        countText.getStyleClass().add("text-body");

        HBox row = new HBox(8, button, countText);
        row.setAlignment(Pos.CENTER_LEFT);

        return new MoverRowUI(button, countText, row);
    }

    public void updateInventoryUI() {
        updateRotation();
        updateMovers();
    }

    private void updateRotation() {
        rotationLabel.setText(
                "Rotation: " + PlacementController.INSTANCE.getRotation()
        );
    }

    private void updateMovers() {
        String selected = inventory.getCurrentSelection();

        for (var entry : inventory.getCurrentAvailableMovers().entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();

            MoverRowUI rowUI = moverRows.get(name);
            if (rowUI != null) {
                rowUI.updateCount(count);
                rowUI.updateAvailability(count != 0);
                rowUI.updateSelection(name.equals(selected));
            }
        }
    }

    private static class MoverRowUI {
        private final Button button;
        private final Text countText;
        private final HBox container;

        MoverRowUI(Button button, Text countText, HBox container) {
            this.button = button;
            this.countText = countText;
            this.container = container;
        }

        HBox getContainer() {
            return container;
        }

        void updateCount(int count) {
            String countStr = (count == -1) ? "∞" : String.valueOf(count);
            countText.setText("x " + countStr);
        }

        void updateAvailability(boolean available) {
            button.setDisable(!available);
        }

        void updateSelection(boolean isSelected) {
            clearStyles();

            if (isSelected) {
                button.getStyleClass().add("button-primary");
            } else if (button.isDisabled()) {
                button.getStyleClass().addAll("text-muted", "button-error");
                countText.getStyleClass().addAll("text-muted", "text-error");
            }
        }

        private void clearStyles() {
            button.getStyleClass().removeAll("button-primary", "button-error", "text-muted");
            countText.getStyleClass().removeAll("text-error", "text-muted");
        }
    }
}
