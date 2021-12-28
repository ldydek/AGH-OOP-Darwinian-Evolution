package agh.ics.project1;
import java.util.ArrayList;


public class Animal implements IMapElement {
    private Vector2d position;
    private int energy;
    private static int moveEnergy;
    private static int startingEnergy;
    private Genes genes;
    private int turn;
    private final IWorldMap map;
    private int lifeLength = 0;
    private ArrayList<IPositionChangeObserver> observers = new ArrayList<>();
    private ArrayList<IEnergyChangeObserver> observers1 = new ArrayList<>();


    public Animal(Vector2d position, IWorldMap map) {
//        kwestia, czy pierwszym np. dziesięciu zwięrzętom dać konkretne pozycje, czy wylosować
        Genes genes = new Genes();
        this.position = position;
        this.turn = (int) Math.round(Math.random() * 7);
        this.genes = genes;
        this.map = map;
        this.energy = startingEnergy;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genes getGenes() {
        return this.genes;
    }

    public void setEnergy(int a) {
        this.energy += a;
    }

    public static void setStartingEnergy(int a) {
        startingEnergy = a;
    }

    public static void setMoveEnergy(int a) {
        moveEnergy = a;
    }

    public static int getStartingEnergy() {
        return startingEnergy;
    }

    @Override
    public int getGene(int a) {
        return this.genes.getGenes()[a];
    }

    public void setGenes(Genes genes) {
        this.genes = genes;
    }

    private static int getRandom(int[] array) {
        int rnd = (int) Math.round(Math.random() * 32);
        return array[rnd];
    }

    private MapDirection change(int a) {
        return switch (this.turn) {
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTHEAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTHEAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTHWEST;
            case 6 -> MapDirection.WEST;
            case 7 -> MapDirection.NORTHWEST;
            default -> throw new IllegalArgumentException(a + " is not legal move specification!");
        };
    }

    public Animal move() {
        int random = genes.getRandom();
        Vector2d newPosition = this.position;
        if ((random != 4) && (random != 0)) {
            this.turn = (this.turn + random) % 8;
        }
//        jak wylosuje 4 to zwierzę się cofa i nie zmienia swojej orientacji
        if (random == 4) {
            newPosition = this.position.add(this.change(this.turn).toUnitVector().opposite());
        }
        if (random == 0) {
            newPosition = this.position.add(this.change(this.turn).toUnitVector());
        }
        energy -= moveEnergy;
        energyChanged(this, this.position);
        this.lifeLength++;
        if (energy <= 0) {
            return this.dead();
        }
        if (this.map.canMoveTo(newPosition) && (this.position != newPosition)) {
            positionChanged(this.position, newPosition, this);
            this.position = newPosition;
        }
//        zmiana położenia zwierzaka
//        strata energii każdego dnia w związku z ruchem lub obrotem
        if (!this.map.canMoveTo(newPosition) && (map.getClass() == MapWithoutBorders.class) && (this.position != newPosition)) {
            int a = (newPosition.getX() + map.getMapWidth()) % map.getMapWidth();
            int b = (newPosition.getX() + map.getMapHeight()) % map.getMapHeight();
            Vector2d newPosition1 = new Vector2d(a, b);
            positionChanged(this.position, newPosition1, this);
            this.position = newPosition1;
        }
        return null;
    }

    @Override
    public String getPath() {
        if (this.energy < startingEnergy/2) return "src/main/resources/animallowenergy.png";
        else return "src/main/resources/animal.png";
    }

    public Animal dead() {
        if (this.map.getHashMap().get(this.position) != null) {
            Animal animal = this.map.getHashMap().get(this.position).removeElement(this);
            if (this.map.getHashMap().get(this.position).getSortedSet().isEmpty()) {
                this.map.getHashMap().remove(this.position);
            }
            return animal;
        }
        return null;
    }
    public String toString() {
        return switch (this.turn) {
            case 0 -> "N";
            case 1 -> "NE";
            case 2 -> "E";
            case 3 -> "SE";
            case 4 -> "S";
            case 5 -> "SW";
            case 6 -> "W";
            case 7 -> "NW";
            default -> throw new IllegalArgumentException(this.turn + " is not legal move specification!");
        };
    }
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition, element);
        }
    }

    public void energyChanged(IMapElement element, Vector2d position) {
        for (IEnergyChangeObserver element1 : observers1) {
            element1.energyChange(element, position);
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public int getLifeLength() {
        return lifeLength;
    }
}
