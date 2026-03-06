package registry.render;

import java.util.HashMap;
import java.util.Map;

import ui.render.Renderer;
import ui.base.*;

/**
 * A registry that maps component classes to their corresponding {@link Renderer}s.
 * This allows the rendering system to dynamically look up the appropriate renderer for any game object.
 */
public final class RendererRegistry {

    /**
     * Map of classes to their registered renderers.
     */
    private final Map<Class<?>, Renderer<?>> renderers = new HashMap<>();

    /**
     * The singleton instance of RendererRegistry.
     */
    public static final RendererRegistry INSTANCE = new RendererRegistry();

    /** 
     * Registers a renderer for a specific type.
     * 
     * @param <T> The type of the object to render.
     * @param type The class of the object.
     * @param renderer The renderer instance to use for this type.
     */
    public <T> void register(Class<T> type, Renderer<? super T> renderer) {
        renderers.put(type, renderer);
    }

    /** 
     * Retrieves the most specific renderer available for a given object instance.
     * 
     * @param <T> The type of the object.
     * @param obj The object instance to find a renderer for.
     * @return The best matching {@link Renderer}, or {@link EmptyTileRenderer} if no match is found.
     */
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
