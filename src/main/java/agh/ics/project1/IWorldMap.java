package agh.ics.project1;

import java.util.Map;

public interface IWorldMap {
    Map<Vector2d, MapField> getHashMap();
    boolean canMoveTo(Vector2d position);
    boolean isOccupied(Vector2d position);
    void addPlantInAJungle();
    void addPlantInASteppe();
    int getMapHeight();
    int getMapWidth();
}
