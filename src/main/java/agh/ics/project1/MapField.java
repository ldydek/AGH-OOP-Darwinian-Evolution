package agh.ics.project1;
import java.util.SortedSet;

public class MapField implements IEnergyChangeObserver {
//    klasa, której obiektami będą elementy mapy znajdujące się na tym samym polu
//    pomaga w rozmnażaniu
//    przy alokacji obiektów tej klasy w inicjalizacji trzeba napisać new TreeSet<>, bo dla każdego pola jest inny

    private SortedSet<IMapElement> sortedSet;
    private IWorldMap map;

    public MapField(SortedSet<IMapElement> sortedSet, AbstractWorldMap map) {
        this.sortedSet = sortedSet;
        this.map = map;
    }

    public void addElement(IMapElement element) {
        sortedSet.add(element);
    }

    public SortedSet<IMapElement> getSortedSet() {
        return sortedSet;
    }

    public IWorldMap getMap() {
        return map;
    }

    public Animal removeElement(IMapElement element) {
        sortedSet.remove(element);
        return (Animal) element;
    }

    public boolean eat() {
//      flagi czy trawa i zwierzak występują na danym polu
        int a = 0;
        int b = 0;
        boolean f1 = false;
        IMapElement element2 = new Plant(new Vector2d(0, 0));
        int energyBoost = 0;
        for (IMapElement element: sortedSet) {
            if (element.getClass() == Plant.class) {
                element2 = element;
                a = 1;
                if (!f1) {
                    energyBoost = element.getEnergy();
                    f1 = true;
                }
            }
            if (element.getClass() == Animal.class) {
                b = 1;
            }
            if ((a == 1) && (b == 1)) {
                break;
            }
        }
        if ((a == 0) || (b == 0)) {
            return false;
        }
//        nie ma trawy na danym polu bądź jest, ale nie ma kto jej jeść - konsumpcja niemożliwa
        int energy = sortedSet.first().getEnergy();
        int ctr = 0;

        for (IMapElement element1: sortedSet) {
            if (element1.getClass() == Animal.class) {
                energy = element1.getEnergy();
                break;
            }
        }

        for (IMapElement element1: sortedSet) {
            if (element1.getClass() == Plant.class) {
                continue;
            }
            if ((element1.getClass() == Plant.class) || (element1.getEnergy() != energy)) {
                break;
            }
            ctr += 1;
        }
        energyBoost /= ctr;
        for (IMapElement element1: sortedSet) {
            if (element1.getClass() == Plant.class) {
                continue;
            }
            if ((element1.getClass() == Plant.class) || (element1.getEnergy() != energy)) {
                break;
            }
            element1.setEnergy(energyBoost);
        }
        sortedSet.remove(element2);
        return true;
    }

//    minimalna ilość energii potrzebnej do kopulacji to połowa energii początkowej zwierzaka
    public Animal copulate() {
        int a = getRandom();
        int ctr = 0;
        int startEnergy = Animal.getStartingEnergy();
        IMapElement element1 = new Animal(new Vector2d(0, 0), map);
        IMapElement element2 = new Animal(new Vector2d(0, 0), map);
        for (IMapElement element: sortedSet) {
            if (ctr == 0) {
                if ((element.getClass() == Animal.class) && (element.getEnergy() >= startEnergy/2)) {
                    ctr += 1;
                    element1 = element;
                    continue;
                }
            }
            if ((ctr == 1) && (element.getClass() == Animal.class) && (element.getEnergy() >= startEnergy/2)) {
                ctr += 1;
                element2 = element;
            }
            if (ctr == 2) {
                break;
            }
        }
        if (ctr < 2) {
            return null;
        }
        int energy1 = element1.getEnergy();
        int energy2 = element2.getEnergy();
        Genes genes = new Genes();
        energy1 /= 4;
        energy2 /= 4;
        element1.setEnergy(-energy1);
        element2.setEnergy(-energy2);
        energyChange(element1, new Vector2d(0, 0));
        energyChange(element2, new Vector2d(0, 0));
        if (energy1 < energy2) {
            int ratio = energy1*32/(energy1 + energy2);
            if (a == 0) {
                for (int i=0; i<32-ratio; i++) {
                    genes.setGene(i, element2.getGene(i));
                }
                for (int i=ratio; i<32; i++) {
                    genes.setGene(i, element1.getGene(i));
                }
            }
            else if (a == 1) {
                for (int i=0; i<ratio; i++) {
                    genes.setGene(i, element1.getGene(i));
                }
                for (int i=32-ratio; i<32; i++) {
                    genes.setGene(i, element2.getGene(i));
                }
            }
        }
        else {
            int ratio = energy2*32/(energy1+energy2);
            if (a == 0) {
                for (int i=0; i<32-ratio; i++) {
                    genes.setGene(i, element2.getGene(i));
                }
                for (int i=32-ratio; i<32; i++) {
                    genes.setGene(i, element1.getGene(i));
                }
            }
            else if (a == 1) {
                for (int i=0; i<ratio; i++) {
                    genes.setGene(i, element1.getGene(i));
                }
                for (int i=ratio; i<32; i++) {
                    genes.setGene(i, element2.getGene(i));
                }
            }
        }
        Animal animal = new Animal(element1.getPosition(), this.map);
        animal.setGenes(genes);
        sortedSet.add(animal);
        animal.setEnergy(-10+energy1+energy2);
        energyChange(animal, new Vector2d(0, 0));
//        randomowy wektor, bo tak narzuca interfejs IEnergyChangeObserver
        return animal;
    }

    public int getRandom() {
        return (int) Math.round(Math.random() * 1);
    }

    @Override
    public void energyChange(IMapElement element, Vector2d position) {
        sortedSet.remove(element);
        sortedSet.add(element);
    }
}
