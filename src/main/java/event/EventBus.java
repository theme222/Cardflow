package event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private static final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners = new HashMap<>();

    public static <T extends Event> Runnable register(Class<T> clazz, EventListener<T> listener) {
        // Make sure to unregister too if you put this in a constructor that can get called multiple times
        listeners.computeIfAbsent(clazz, k -> new ArrayList<>()).add(listener);
        return () -> unregister(clazz, listener);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Event> void emit(T event) {
        List<EventListener<? extends Event>> list = listeners.get(event.getClass());
        if (list == null) return;
        for (EventListener<? extends Event> l : list) {
            ((EventListener<T>) l).onEvent(event);
        }
    }

    private static <T extends Event> void unregister(Class<T> clazz, EventListener<T> listener) {
        // you better keep track of that unregistration that got returned
        // using method reference or lambda functions don't give you the same listener. (thats why I made it return the unregistrator)
        List<EventListener<? extends Event>> list = listeners.get(clazz);
        if (list == null) return;

        System.out.println("Removing " + listener.getClass().getSimpleName());
        list.remove(listener);

        // Optional cleanup to prevent memory leak
        if (list.isEmpty()) {
            listeners.remove(clazz);
        }
    }
}
