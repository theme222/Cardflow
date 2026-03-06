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

/**
 * The {@code LevelInfoPane} is the primary sidebar UI component that displays 
 * level metadata, player inventory, and game controls.
 * <p>
 * It provides a real-time view of the game phase, allows the player to select 
 * movers from their inventory, and hosts the playback controls (Play, Pause, Step, Reset).
 */
public class LevelInfoPane extends VBox {

    /**
     * Represents the different logical phases the game can be in.
     */
    private enum GamePhase {
        /** The engine is currently moving cards. */
        MOVING("Current Phase: Moving   "),
        /** The engine is currently applying tile modifications. */
        MODIFYING("Current Phase: Modifying"),
        /** The engine is paused. */
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

    /**
     * Constructs a {@code LevelInfoPane} and initializes its sub-components.
     * @param tooltipLayer The layer used to display tooltips for UI elements.
     */
    public LevelInfoPane(TooltipLayer tooltipLayer) {
        inventory = PlayerInventory.getInstance();

        initializeStyle();
        initializeComponents(tooltipLayer);
        buildLayout(tooltipLayer);
        initializeContent(tooltipLayer);
    }

    /** Sets up the general CSS styling and layout for the pane. */
    private void initializeStyle() {
        setPadding(new Insets(12));
        setSpacing(10);
        setAlignment(Pos.CENTER);
    }

    /** 
     * Creates the individual UI nodes like labels and sub-panes.
     * @param tooltipLayer The tooltip layer.
     */
    private void initializeComponents(TooltipLayer tooltipLayer) {
        titleText = createTitleText();
        rotationLabel = createRotationLabel();
        phaseLabel = createPhaseLabel();
        controlPanel = new HBox(10);
        moversList = new VBox(6);
        cardInputListPane = new CardInputListPane("Input Cards", GameLevel.getInstance().INPUT_CARDS, tooltipLayer);
        cardOutputListPane = new CardOutputListPane("Output Cards", GameLevel.getInstance().OUTPUT_CARDS, tooltipLayer);
    }

    /** @return A styled text node for the level name. */
    private Text createTitleText() {
        Text text = new Text(GameLevel.getInstance().LEVELNAME);
        text.getStyleClass().add("text-heading");
        return text;
    }

    /** @return A label for displaying the current placement rotation. */
    private Label createRotationLabel() {
        Label label = new Label();
        label.getStyleClass().add("text-body");
        return label;
    }

    /** @return A label for displaying the active {@link GamePhase}. */
    private Label createPhaseLabel() {
        return new Label(GamePhase.PAUSED.getDisplayText());
    }

    /** 
     * Orchestrates the final layout of the pane.
     * @param tooltipLayer The tooltip layer.
     */
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

    /** 
     * Populates the pane with initial data and attaches listeners.
     * @param tooltipLayer The tooltip layer.
     */
    private void initializeContent(TooltipLayer tooltipLayer) {
        buildMoverRows(tooltipLayer);
        buildControlPanel();
        updateInventoryUI();
        registerEventHandlers();
    }

    /** @return A container for status-related labels. */
    private HBox createStatusContainer() {
        HBox statusContainer = new HBox(10);
        statusContainer.setAlignment(Pos.CENTER);
        statusContainer.getChildren().addAll(phaseLabel, rotationLabel);
        return statusContainer;
    }

    /** Attaches listeners to the global event bus to update the phase label. */
    private void registerEventHandlers() {
        unregisterAfterMovement = EventBus.register(MovementEndedEvent.class, e -> updatePhase(GamePhase.MOVING));
        unregisterAfterModify = EventBus.register(ModifyEndedEvent.class, e -> updatePhase(GamePhase.MODIFYING));
        unregisterAfterPause = EventBus.register(PausedEvent.class, e -> updatePhase(GamePhase.PAUSED));
    }

    /** 
     * Updates the text of the phase label.
     * @param phase The new phase to display.
     */
    private void updatePhase(GamePhase phase) {
        phaseLabel.setText(phase.getDisplayText());
    }

    /**
     * Unregisters all event listeners to prevent memory leaks.
     */
    public void cleanup() {
        unregisterAfterMovement.run();
        unregisterAfterModify.run();
        unregisterAfterPause.run();
        cardOutputListPane.cleanup();
    }

    /** Populates the control panel with playback buttons. */
    private void buildControlPanel() {
        controlPanel.getChildren().addAll(
                createButton("Play ▶", "button-success", TickEngine::play),
                createButton("Pause ǁ", "button-warning", TickEngine::pause),
                createButton("Step →", "button-info", TickEngine::step),
                createButton("Reset ☕", "button-error", TickEngine::reset)
        );
    }

    /** 
     * Utility to create a styled button with a click sound and action.
     * @param text The button label.
     * @param styleClass The CSS class to apply.
     * @param action The runnable to execute on click.
     * @return A configured {@link Button}.
     */
    private Button createButton(String text, String styleClass, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setOnAction(e -> {
            AudioManager.playSoundEffect("button-click");
            action.run();
        });
        return button;
    }

    /** 
     * Generates the list of interactive mover buttons from the player's inventory.
     * @param tooltipLayer The tooltip layer.
     */
    private void buildMoverRows(TooltipLayer tooltipLayer) {
        moversList.getChildren().clear();
        moverRows.clear();

        for (String name : inventory.getCurrentAvailableMovers().keySet()) {
            MoverRowUI rowUI = createMoverRow(name, tooltipLayer);
            moverRows.put(name, rowUI);
            moversList.getChildren().add(rowUI.getContainer());
        }
    }

    /** 
     * Creates an individual row for a mover in the sidebar.
     * @param name The internal ID of the mover.
     * @param tooltipLayer The tooltip layer.
     * @return A {@code MoverRowUI} container.
     */
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

    /**
     * Refreshes the entire sidebar UI based on the current inventory state.
     */
    public void updateInventoryUI() {
        updateRotation();
        updateMovers();
    }

    /** Syncs the rotation label with the placement controller. */
    private void updateRotation() {
        rotationLabel.setText(
                "Rotation: " + PlacementController.INSTANCE.getRotation()
        );
    }

    /** Syncs the counts and selection status of all mover rows. */
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

    /**
     * Inner helper class for managing the UI state of a single mover row.
     */
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
