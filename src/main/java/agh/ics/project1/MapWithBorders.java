package agh.ics.project1;

import java.util.*;

public class MapWithBorders extends AbstractWorldMap {

    public MapWithBorders(int mapHeight, int mapWidth, int jungleMapRatio, int animalQuantity) {
        super(mapHeight, mapWidth, jungleMapRatio, animalQuantity);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(mapLowerLeft) && position.precedes(mapUpperRight);
    }

    public Map<Vector2d, MapField> getHashMap() {
        return this.hashMap;
    }
}
