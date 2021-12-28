package agh.ics.project1;

import java.util.Objects;

public class Plant implements IMapElement {
    private final Vector2d position;
    private static int plantEnergy;

    public Plant(Vector2d position) {
        this.position = position;
    }

    public String toString() {
        return "*";
    }

    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public int getEnergy() {
        return plantEnergy;
    }

    public void setEnergy(int a) {
        plantEnergy = a;
    }

    @Override
    public int getGene(int a) {
        return 0;
    }

    @Override
    public Animal move() {
        return null;
    }

    @Override
    public String getPath() {
        return "src/main/resources/plant.png";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(position, plant.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, plantEnergy);
    }

    public static void setPlantEnergy(int a) {
        plantEnergy = a;
    }
}
