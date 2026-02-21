package application.view;

import application.ViewManager;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import logic.GameLevel;
import logic.PlayerInventory;
import ui.button.BackButton;
import ui.game.GameRenderStack;
import ui.game.GameTilePane;
import ui.inventory.InventoryPane;
import application.Game;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import util.Direction;
import util.GridPos;
import event.EventBus;
import event.RenderEvent;

public class GameView extends View {

    private static GameView instance; // This value can be null and can be invalid (just because instance exists doesn't mean that it is the current view. You must check it on the viewManager.)
    private InventoryPane inventoryPane;

    final private GameRenderStack gameGrid;

    public void updateTileAndAdjacent(GridPos pos) {
        updateIfValid(pos);

        updateIfValid(pos.addDirection(Direction.RIGHT)); // right
        updateIfValid(pos.addDirection(Direction.LEFT)); // left
        updateIfValid(pos.addDirection(Direction.DOWN)); // down
        updateIfValid(pos.addDirection(Direction.UP)); // up
    }

    private void updateIfValid(GridPos pos) {
        gameGrid.updateIfValid(pos);
    }

    public GameView(GameLevel level) {
        super();
        setInstance(this);
        
        GameLevel.setInstance(level); // Most components will rely on this
        PlayerInventory.setInstance(new PlayerInventory(level));

        // TODO: MODIFY THIS TO BE A REGULAR PANE AND ADD A TILING MANAGER TO ALLOW GOOD
        // LOOKING ANIMS AND STUFF :D
        
        gameGrid = new GameRenderStack(level);

        VBox infoPane = new VBox();
        infoPane.setPadding(new Insets(10));

        inventoryPane = new InventoryPane(PlayerInventory.getInstance());

        HBox mainLayout = new HBox();
        mainLayout.getChildren().addAll(gameGrid, inventoryPane);

        mainLayout.setAlignment(Pos.TOP_CENTER);


        root.getChildren().addAll(mainLayout, new BackButton());
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));


        // Register to receive render events and update affected tiles
        EventBus.register(RenderEvent.class, ev -> {
            for (GridPos point : ev.getChangedPoints()) {
                updateIfValid(point);
            }
        });
    }

    public InventoryPane getInventoryPane() {
        return inventoryPane;
    }

    public void setInventoryPane(InventoryPane inventoryPane) {
        this.inventoryPane = inventoryPane;
    }

    public static GameView getInstance() {
        return instance;
    }

    public static void setInstance(GameView view) {
        instance = view;
    }
}
