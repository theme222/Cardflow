package placement;

import java.util.Set;

import application.ViewManager;
import application.view.GameView;
import component.GameTile;
import javafx.scene.input.MouseButton;
import logic.PlayerInventory;
import util.GridPos;

public class PlacementController { // Is this supposed to be a user interaction controller?

    public Set<GridPos> handleTileClick(
            GameTile tile,
            MouseButton button,
            boolean shift,
            boolean ctrl
    ) { // replacing most of this to calls to PlayerInventory to allow for dynamic selection.
        if (button == MouseButton.PRIMARY) {
            if (tile.getMover() == null)
                PlayerInventory.getInstance().placeToGrid(tile.getGridPos());
            else
                PlayerInventory.getInstance().removeFromGrid(tile.getGridPos());
        }

        if (button == MouseButton.SECONDARY) {
            if (tile.getMover() != null)
                tile.getMover().rotate();
            else
                PlayerInventory.getInstance().cycleRotation();
        }

        GameView.getInstance().getInventoryPane().updateUI(); // Not sure if this is the best place to put it
        System.out.println("Clicked tile at " + tile.getGridPos());
        return Set.of(tile.getGridPos());
    }

    public void handleSceneClick(
            MouseButton button,
            boolean shift,
            boolean ctrl
    ) {
        if (button == MouseButton.SECONDARY) {
            PlayerInventory.getInstance().cycleRotation();
            GameView.getInstance().getInventoryPane().updateUI();
        }
    }
}
