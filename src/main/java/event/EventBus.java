package event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A static event bus that manages event registration and emission.
 * It allows components to subscribe to specific event types and receive notifications when those events occur.
 */
public class EventBus {
    /**
     * A map storing lists of event listeners for each event class type.
     */
    private static final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners = new HashMap<>();

    /**
     * Registers a listener for a specific event type.
     * 
     * @param <T> The type of Event.
     * @param clazz The class of the event to listen for.
     * @param listener The listener to be invoked when the event occurs.
     * @return A {@link Runnable} that, when executed, will unregister the listener.
     */
    public static <T extends Event> Runnable register(Class<T> clazz, EventListener<T> listener) {
        // Make sure to unregister too if you put this in a constructor that can get called multiple times
        listeners.computeIfAbsent(clazz, k -> new ArrayList<>()).add(listener);
        return () -> unregister(clazz, listener);
    }

    /**
     * Emits an event to all registered listeners for its class type.
     * 
     * @param <T> The type of Event.
     * @param event The event instance to emit.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Event> void emit(T event) {
        List<EventListener<? extends Event>> list = listeners.get(event.getClass());
        if (list == null) return;
        for (EventListener<? extends Event> l : list) {
            ((EventListener<T>) l).onEvent(event);
        }
    }

    /**
     * Unregisters a specific listener from an event type.
     * 
     * @param <T> The type of Event.
     * @param clazz The class of the event the listener was registered for.
     * @param listener The listener instance to remove.
     */
    private static <T extends Event> void unregister(Class<T> clazz, EventListener<T> listener) {
        // you better keep track of that unregistration that got returned
        // using method reference or lambda functions don't give you the same listener. (thats why I made it return the unregistrator)
        List<EventListener<? extends Event>> list = listeners.get(clazz);
        if (list == null) return;

        list.remove(listener);

        // Optional cleanup to prevent memory leak
        if (list.isEmpty()) {
            listeners.remove(clazz);
        }
    }
}
