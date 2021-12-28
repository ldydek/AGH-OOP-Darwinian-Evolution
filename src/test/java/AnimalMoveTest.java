import agh.ics.project1.Animal;
import agh.ics.project1.MapWithBorders;
import agh.ics.project1.Vector2d;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AnimalMoveTest {
    MapWithBorders map = new MapWithBorders(10, 10, 60, 10);
    Animal animal1 = new Animal(new Vector2d(2, 2), map);
    Animal animal2 = new Animal(new Vector2d(3, 4), map);
    Animal animal3 = new Animal(new Vector2d(3, 4), map);
    Animal animal4 = new Animal(new Vector2d(6, 2), map);
    Animal animal5 = new Animal(new Vector2d(7, 1), map);

//    @Test
//    public void moveTest() {
//        int rnd = (int) Math.round(Math.random() * 8);
//        switch (rnd) {
//            case 0
//        }
//    }
}
