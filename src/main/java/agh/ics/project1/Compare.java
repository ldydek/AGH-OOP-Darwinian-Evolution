package agh.ics.project1;

import java.util.Comparator;

public class Compare implements Comparator<IMapElement> {
    //    klasa do porównywania elementów mapy z SortedSet w klasie MapField
    @Override
    public int compare(IMapElement o1, IMapElement o2) {
        if (o1.equals(o2)) {
            return 0;
        }
        else if (o1.getEnergy() < o2.getEnergy()) {
            return 1;
        }
        else if (o1.getEnergy() > o2.getEnergy()) {
            return -1;
        }
        else {
            if (o1.getClass() == Animal.class) {
                return -1;
            }
            else return 1;
        }
    }
}
