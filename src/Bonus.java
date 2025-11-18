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

    public int calculateBonus(Player p){
        int bonus = 0;
        //TODO: implement for all bonus types
        //place holder for future logic (by bonus type)
        for (String bName : birds){
            for (Bird b : p.getCardsInHand()){
                if (b.getName().equals(bName)){
                    bonus += 1;
                }
            }
        }
        return bonus;
    }
    
}