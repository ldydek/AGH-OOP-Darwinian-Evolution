package agh.ics.project1;


import java.util.*;

public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver, IEnergyChangeObserver {
//    nie będzie metody isOccupied, bo tym razem na jednym polu może być dużo elementów
//    klasa MapWithBorders będzie zawierać dodatkową metodę uniemożliwiającą wyjście poza mapę (canMoveTo)
    protected final Vector2d jungleLowerLeft;
    protected final Vector2d jungleUpperRight;
    protected final Vector2d mapLowerLeft;
    protected final Vector2d mapUpperRight;
    protected int mapHeight;
    protected int mapWidth;
    protected Map<Vector2d, MapField> hashMap = new LinkedHashMap<>();
    protected Map<Genes, Integer> numberOfGenomes = new LinkedHashMap<>();
    protected int a = getRandomNumber(0, mapHeight);
    protected int b = getRandomNumber(0, mapWidth);
    protected Vector2d position = new Vector2d(a, b);

    public AbstractWorldMap(int mapHeight, int mapWidth, int jungleMapRatio, int animalQuantity) {
        int jungleHeight = mapHeight*jungleMapRatio/100;
        int jungleWidth = mapWidth*jungleMapRatio/100;
        this.mapLowerLeft = new Vector2d(0, 0);
        this.mapUpperRight = new Vector2d(mapHeight-1, mapWidth-1);
        this.jungleLowerLeft = new Vector2d((mapHeight-jungleHeight)/2, (mapWidth-jungleWidth)/2);
        this.jungleUpperRight = new Vector2d((mapHeight+jungleHeight)/2, (mapWidth+jungleWidth)/2);
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        for (int i=0; i<animalQuantity; i++) {
            addAnimalInAMap(mapHeight, mapWidth);
        }
    }

    @Override
    public Map<Vector2d, MapField> getHashMap() {
        return this.hashMap;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return true;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    protected void addAnimalInAMap(int mapHeight, int mapWidth) {
        while (isOccupied(position)) {
            a = getRandomNumber(0, mapHeight);
            b = getRandomNumber(0, mapWidth);
            position = new Vector2d(a, b);
        }
        Animal animal = new Animal(new Vector2d(a, b), this);
        animal.addObserver(this);
        hashMap.put(position, new MapField(new TreeSet<>(new Compare()), this));
        hashMap.get(position).addElement(animal);
    }

    public void addPlantInAJungle() {
        ArrayList<Vector2d> freeFields = new ArrayList<>();
        for (int i = jungleLowerLeft.getY(); i <= jungleUpperRight.getY(); i++) {
            for (int j = jungleLowerLeft.getX(); j <= jungleUpperRight.getX(); j++) {
                if (isOccupied(new Vector2d(i, j))) continue;
                freeFields.add(new Vector2d(i, j));
            }
        }
        if (!freeFields.isEmpty()) {
            Collections.shuffle(freeFields);
            Vector2d position = new Vector2d(freeFields.get(0).getX(), freeFields.get(0).getY());
            hashMap.put(position, new MapField(new TreeSet<>(new Compare()),this));
            hashMap.get(position).addElement(new Plant(freeFields.get(0)));
        }
    }

    public void addPlantInASteppe() {
        ArrayList<Vector2d> freeFields = new ArrayList<>();
        for (int i = mapLowerLeft.getY(); i <= mapUpperRight.getY(); i++) {
            for (int j = mapLowerLeft.getX(); j <= mapUpperRight.getX(); j++) {
                Vector2d position = new Vector2d(i, j);
                if (!(position.follows(jungleLowerLeft)) || !(position.precedes(jungleUpperRight))) {
                    if (isOccupied(position)) continue;
                    freeFields.add(position);
                }
            }
        }
        if (!freeFields.isEmpty()) {
            Collections.shuffle(freeFields);
            Vector2d position = new Vector2d(freeFields.get(0).getX(), freeFields.get(0).getY());
            hashMap.put(position, new MapField(new TreeSet<>(new Compare()), this));
            hashMap.get(position).addElement(new Plant(new Vector2d(a, b)));
        }
    }

    public boolean isOccupied(Vector2d position) {
        return hashMap.containsKey(position);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element) {
        if (hashMap.get(oldPosition) != null) {
            hashMap.get(oldPosition).removeElement(element);
            if (hashMap.get(oldPosition).getSortedSet().isEmpty()) {
                hashMap.remove(oldPosition);
            }
            if (hashMap.containsKey(newPosition)) {
                hashMap.get(newPosition).addElement(element);
            } else {
                hashMap.put(newPosition, new MapField(new TreeSet<>(new Compare()), this));
                hashMap.get(newPosition).addElement(element);
            }
        }
        else {
            hashMap.remove(oldPosition);
        }
    }

    @Override
    public void energyChange(IMapElement element, Vector2d position) {
        hashMap.get(position).removeElement(element);
        hashMap.get(position).addElement(element);
    }

    public Vector2d getMapLowerLeft() {
        return mapLowerLeft;
    }

    public Vector2d getMapUpperRight() {
        return mapUpperRight;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void eat() {
        for (Vector2d position: hashMap.keySet()) {
            hashMap.get(position).eat();
        }
    }

    public Map<Genes, Integer> getNumberOfGenome() {
        return numberOfGenomes;
    }

    public boolean jungleField(Vector2d position) {
        return position.follows(jungleLowerLeft) && position.precedes(jungleUpperRight);
    }

    public int animalQuantity() {
        int animalQuantity = 0;
        for (Vector2d position: hashMap.keySet()) {
            for (IMapElement element: hashMap.get(position).getSortedSet()) {
                if (element.getClass() == Animal.class) animalQuantity++;
            }
        }
        return animalQuantity;
    }

    public int plantQuantity() {
        int plantQuantity = 0;
        for (Vector2d position: hashMap.keySet()) {
            for (IMapElement element: hashMap.get(position).getSortedSet()) {
                if (element.getClass() == Plant.class) plantQuantity++;
            }
        }
        return plantQuantity;
    }

    public int averageEnergy() {
        int averageEnergy = 0;
        int animalQuantity = 0;
        for (Vector2d position: hashMap.keySet()) {
            for (IMapElement element: hashMap.get(position).getSortedSet()) {
                if (element.getClass() == Animal.class) {
                    averageEnergy += element.getEnergy();
                    animalQuantity++;
                }
            }
        }
        if (animalQuantity != 0 ) return averageEnergy/animalQuantity;
        else return -10000;
    }

    public Genes dominantGenome() {
        int a = -1;
        Genes genes = null;
        for (Genes genome: numberOfGenomes.keySet()) {
            if (numberOfGenomes.get(genome) > a) {
                a = numberOfGenomes.get(genome);
                genes = genome;
            }
        }
        return genes;
    }
}
