package event;

import util.GridPos;
import java.util.Set;

/**
 * An event representing a request or notification that specific grid positions need to be re-rendered.
 */
public class RenderEvent implements Event {
    /**
     * The set of grid positions that have changed and require rendering.
     */
    private final Set<GridPos> changedPoints;

    /**
     * Constructs a new RenderEvent.
     * 
     * @param changedPoints A set of {@link GridPos} representing the modified grid locations.
     */
    public RenderEvent(Set<GridPos> changedPoints) {
        this.changedPoints = changedPoints;
    }

    /**
     * Gets the set of grid positions that need to be re-rendered.
     * 
     * @return A set of {@link GridPos} objects.
     */
    public Set<GridPos> getChangedPoints() {
        return changedPoints;
    }
}
