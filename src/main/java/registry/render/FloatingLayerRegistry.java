package registry.render;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.scene.layout.Pane;

/**
 * A registry for managing different rendering layers and their associated JavaFX Panes.
 * It also tracks which layers are considered "floating" (e.g., UI overlays or animations).
 */
public final class FloatingLayerRegistry {

    /** Maps each {@link RenderLayer} to its corresponding JavaFX {@link Pane}. */
    private final Map<RenderLayer, Pane> panes = new HashMap<>();
    /** Set of layers that are marked as floating. */
    private final Set<RenderLayer> floatingLayers = new HashSet<>();

    /** The singleton instance of FloatingLayerRegistry. */
    public static final FloatingLayerRegistry INSTANCE = new FloatingLayerRegistry();

    /** 
     * Marks a layer as being a floating layer.
     * 
     * @param layer The {@link RenderLayer} to mark.
     */
    public void markFloating(RenderLayer layer) {
        floatingLayers.add(layer);
    }

    /** 
     * Checks if a layer is marked as floating.
     * 
     * @param layer The {@link RenderLayer} to check.
     * @return {@code true} if the layer is floating, {@code false} otherwise.
     */
    public boolean isFloating(RenderLayer layer) {
        return floatingLayers.contains(layer);
    }

    /** 
     * Registers a JavaFX Pane for a specific render layer.
     * 
     * @param layer The {@link RenderLayer}.
     * @param pane The {@link Pane} to associate with the layer.
     */
    public void registerEntry(RenderLayer layer, Pane pane) {
        panes.put(layer, pane);
    }

    /** 
     * Retrieves the JavaFX Pane associated with a render layer.
     * 
     * @param layer The {@link RenderLayer}.
     * @return The associated {@link Pane}, or {@code null} if not registered.
     */
    public Pane getPane(RenderLayer layer) {
        return panes.get(layer);
    }
}
