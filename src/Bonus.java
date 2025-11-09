package src;
import java.util.*;

public class Bonus {
    final private String name;
    final private ArrayList<Bird> birds;

    public Bonus(String n, ArrayList<Bird> b){
        name = n;
        birds = b;
    }

    public String getName() {return name;}
    public ArrayList<Bird> getBirds() {return birds;}

    public boolean hasBird(Bird b) {return birds.contains(b);}
}
