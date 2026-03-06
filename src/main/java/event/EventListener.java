package event;

/**
 * A functional interface for listening to specific types of events.
 * 
 * @param <T> The type of Event this listener handles.
 */
@FunctionalInterface
public interface EventListener<T extends Event> {
    /**
     * Invoked when an event of type T is emitted.
     * 
     * @param event The event instance that was emitted.
     */
    void onEvent(T event);
}
