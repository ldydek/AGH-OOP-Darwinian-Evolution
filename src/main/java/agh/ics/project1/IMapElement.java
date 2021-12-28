package agh.ics.project1;

public interface IMapElement {
    String toString();
    Vector2d getPosition();
    int getEnergy();
    int getGene(int a);
    Animal move();
    String getPath();
    void setEnergy(int a);
}
