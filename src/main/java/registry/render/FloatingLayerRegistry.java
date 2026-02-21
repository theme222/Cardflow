package registry.render;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.scene.layout.Pane;

public final class FloatingLayerRegistry {

    private final Map<RenderLayer, Pane> panes = new HashMap<>();
    private final Set<RenderLayer> floatingLayers = new HashSet<>();

    public static final FloatingLayerRegistry INSTANCE = new FloatingLayerRegistry();

    public void markFloating(RenderLayer layer) {
        floatingLayers.add(layer);
    }

    public boolean isFloating(RenderLayer layer) {
        return floatingLayers.contains(layer);
    }

    public void registerEntry(RenderLayer layer, Pane pane) {
        panes.put(layer, pane);
    }

    public Pane getPane(RenderLayer layer) {
        return panes.get(layer);
    }
}
