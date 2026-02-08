package util;

public interface GridIndexable {

    GridPos getGridPos();
    void setGridPos(GridPos gridPos);

    boolean isBlocking(); // This varies between classes
}
