package registry.render;

import java.util.HashMap;
import java.util.Map;

import component.GameTile;
import component.card.Card;
import component.mover.Conveyor;
import component.mover.FlipFlop;
import component.mover.ParityFilter;
import component.mover.RedBlackFilter;
import ui.render.Renderer;
import ui.base.*;
import ui.card.CardRenderer;
import ui.mover.*;

public final class RendererRegistry {

    private final Map<Class<?>, Renderer<?>> renderers = new HashMap<>();

    public static final RendererRegistry INSTANCE = new RendererRegistry();

    private RendererRegistry() {
        register(Card.class, CardRenderer.INSTANCE);

        register(GameTile.class, EmptyTileRenderer.INSTANCE);
        register(Conveyor.class, ConveyorRenderer.INSTANCE);
        register(FlipFlop.class, FlipFlopRenderer.INSTANCE);
        register(ParityFilter.class, ParityFilterRenderer.INSTANCE);
        register(RedBlackFilter.class, RedBlackFilterRenderer.INSTANCE);
    }

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
