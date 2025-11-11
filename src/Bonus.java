package src;
import java.awt.image.BufferedImage;
import java.util.*;

public class Bonus {
    final private String name;
    final private ArrayList<String> birds;
    BufferedImage image;

    public Bonus(String n, ArrayList<String> b){
        name = n;
        birds = b;
    }

    public String getName() {return name;}
    public ArrayList<String> getBirds() {return birds;}
    public void setImage(BufferedImage i){image = i;}
    public BufferedImage getImage() {return image;}

    public boolean hasBird(Bird b) {return birds.contains(b.getName());}
}
