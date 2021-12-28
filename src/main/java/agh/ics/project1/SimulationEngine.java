package agh.ics.project1;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SimulationEngine implements Runnable {
    private final AbstractWorldMap map1;
    private final AbstractWorldMap map2;
    private List<IMapElement> animals1 = new LinkedList<>();
    private List<IMapElement> animals2 = new LinkedList<>();
    private List<IPositionChangeObserver> observers = new ArrayList<>();
    private final int moveDelay;


    public SimulationEngine(AbstractWorldMap map1, AbstractWorldMap map2) {
        this.map1 = map1;
        this.map2 = map2;
        this.moveDelay = 50;
        for (Vector2d position: map1.getHashMap().keySet()) {
            for (IMapElement element: map1.getHashMap().get(position).getSortedSet()) {
                if (element.getClass() == Plant.class) continue;
                else {
                    animals1.add(element);
                    if (map1.getNumberOfGenome().get(((Animal) element).getGenes()) == null) {
                        map1.getNumberOfGenome().put(((Animal) element).getGenes(), 0);
                    }
                    else {
                        int a = map1.getNumberOfGenome().get(((Animal) element).getGenes());
                        map1.getNumberOfGenome().put(((Animal) element).getGenes(), a+1);
                    }
                }
            }
        }
        for (Vector2d position: map2.getHashMap().keySet()) {
            for (IMapElement element: map2.getHashMap().get(position).getSortedSet()) {
                if (element.getClass() == Plant.class) continue;
                else {
                    animals2.add(element);
                    if (map2.getNumberOfGenome().get(((Animal) element).getGenes()) == null) {
                        map2.getNumberOfGenome().put(((Animal) element).getGenes(), 0);
                    }
                    else {
                        int a = map2.getNumberOfGenome().get(((Animal) element).getGenes());
                        map2.getNumberOfGenome().put(((Animal) element).getGenes(), a+1);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while (!(animals1.size() == 0) && (!(animals2.size()==0))) {
            System.out.println(animals1.size());
            System.out.println(animals2.size());
            ArrayList<Animal> deadAnimals1 = new ArrayList<>();
            ArrayList<Animal> bornAnimals1 = new ArrayList<>();
            ArrayList<Animal> deadAnimals2 = new ArrayList<>();
            ArrayList<Animal> bornAnimals2 = new ArrayList<>();
            map1.addPlantInAJungle();
            map1.addPlantInASteppe();
            map1.eat();
            map2.addPlantInAJungle();
            map2.addPlantInASteppe();
            map2.eat();
            for (IMapElement animal : animals1) {
                if (animal.move() != null) {
                    deadAnimals1.add((Animal) animal);
                }
                Vector2d position = animal.getPosition();
                if (map1.getHashMap().get(position) == null) continue;
                Animal object = map1.getHashMap().get(position).copulate();
                if (object != null) {
                    bornAnimals1.add(object);
                }
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (IMapElement animal : animals2) {
                if (animal.move() != null) {
                    deadAnimals2.add((Animal) animal);
                }
                Vector2d position = animal.getPosition();
                if (map2.getHashMap().get(position) == null) continue;
                Animal object = map2.getHashMap().get(position).copulate();
                if (object != null) {
                    bornAnimals2.add(object);
                }
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            animals1.removeAll(deadAnimals1);
            animals1.addAll(bornAnimals1);
            animals2.removeAll(deadAnimals2);
            animals2.addAll(bornAnimals2);
            System.out.println(animals1.size() + " " + map1.getHashMap().size());
            System.out.println(animals2.size() + " " + map2.getHashMap().size());
            positionChanged(new Vector2d(0, 0), new Vector2d(0, 0), new Plant(new Vector2d(0, 0)));
        }
        map1Run();
        map2Run();
        positionChanged(new Vector2d(0, 0), new Vector2d(0, 0), new Plant(new Vector2d(0, 0)));
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void map1Run() {
        while (!(animals1.isEmpty())) {
            ArrayList<Animal> deadAnimals1 = new ArrayList<>();
            ArrayList<Animal> bornAnimals1 = new ArrayList<>();
            map1.addPlantInAJungle();
            map1.addPlantInASteppe();
            map1.eat();
            for (IMapElement animal : animals1) {
                if (animal.move() != null) {
                    deadAnimals1.add((Animal) animal);
                }
                if (animal.getEnergy() <= 0) deadAnimals1.add((Animal) animal);
                Vector2d position = animal.getPosition();
                if (map1.getHashMap().get(position) == null) continue;
                Animal object = map1.getHashMap().get(position).copulate();
                if (object != null) {
                    bornAnimals1.add(object);
                }
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            animals1.removeAll(deadAnimals1);
            animals1.addAll(bornAnimals1);
            System.out.println("Pierwsza mapa: " + animals1.size() + " " + map1.getHashMap().size());
            positionChanged(new Vector2d(0, 0), new Vector2d(0, 0), new Plant(new Vector2d(0, 0)));
        }
    }

    public void map2Run() {
        while (!(animals2.isEmpty())) {
            ArrayList<Animal> deadAnimals2 = new ArrayList<>();
            ArrayList<Animal> bornAnimals2 = new ArrayList<>();
            map2.addPlantInAJungle();
            map2.addPlantInASteppe();
            map2.eat();
            for (IMapElement animal : animals2) {
                if (animal.move() != null) {
                    deadAnimals2.add((Animal) animal);
                }
                if (animal.getEnergy() <= 0) deadAnimals2.add((Animal) animal);
                Vector2d position = animal.getPosition();
                if (map2.getHashMap().get(position) == null) continue;
                Animal object = map2.getHashMap().get(position).copulate();
                if (object != null) {
                    bornAnimals2.add(object);
                }
                try {
                    Thread.sleep(moveDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            animals2.removeAll(deadAnimals2);
            animals2.addAll(bornAnimals2);
            System.out.println("Druga mapa: " + animals2.size() + " " + map2.getHashMap().size());
            positionChanged(new Vector2d(0, 0), new Vector2d(0, 0), new Plant(new Vector2d(0, 0)));
        }
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(oldPosition, newPosition, element);
        }
    }
}
