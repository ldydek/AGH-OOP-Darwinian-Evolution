package agh.ics.project1;


import java.util.Map;

public class MapWithoutBorders extends AbstractWorldMap {

    public MapWithoutBorders(int mapHeight, int mapWidth, int jungleMapRatio, int animalQuantity) {
        super(mapHeight, mapWidth, jungleMapRatio, animalQuantity);
    }

    public boolean canMoveTo(Vector2d position) {
        return position.follows(mapLowerLeft) && position.precedes(mapUpperRight);
    }

    public Map<Vector2d, MapField> getHashMap() {
        return this.hashMap;
    }
}
