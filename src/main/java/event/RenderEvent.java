package event;

import util.GridPos;
import java.util.Set;

public class RenderEvent implements Event {
    private final Set<GridPos> changedPoints;

    public RenderEvent(Set<GridPos> changedPoints) {
        this.changedPoints = changedPoints;
    }

    /** 
     * @return Set<GridPos>
     */
    public Set<GridPos> getChangedPoints() {
        return changedPoints;
    }
}
