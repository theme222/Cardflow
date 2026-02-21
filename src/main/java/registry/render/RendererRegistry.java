package registry.render;

import java.util.HashMap;
import java.util.Map;

import ui.render.Renderer;
import ui.base.*;

public final class RendererRegistry {

    private final Map<Class<?>, Renderer<?>> renderers = new HashMap<>();

    public static final RendererRegistry INSTANCE = new RendererRegistry();

    public <T> void register(Class<T> type, Renderer<? super T> renderer) {
        renderers.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public <T> Renderer<T> getRenderer(T obj) {
        Class<?> objClass = obj.getClass();

        Class<?> bestMatch = null;

        for (Class<?> registered : renderers.keySet()) {
            if (registered.isAssignableFrom(objClass)) {
                if (bestMatch == null ||
                    bestMatch.isAssignableFrom(registered)) {
                    bestMatch = registered;
                }
            }
        }

        if (bestMatch == null) {
            return (Renderer<T>) EmptyTileRenderer.INSTANCE;
        }

        return (Renderer<T>) renderers.get(bestMatch);
    }
}
