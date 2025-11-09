package src;
import java.util.*;
import static java.lang.System.*;

public class Board {
    public ArrayList<Bird> forest = new ArrayList<Bird>();
    public ArrayList<Bird> plains = new ArrayList<Bird>();
    public ArrayList<Bird> wetlands = new ArrayList<Bird>();
    
    public void addBird(String habitat, Bird b){
        switch (habitat){
            case ("forest"): forest.add(b);break;
            case ("plains"): plains.add(b);break;
            case ("wetlands"): wetlands.add(b);break;
            default: out.println(habitat + " is not valid");break;
        }
    }
    public void addForestBird(Bird b) {forest.add(b);}
    public void addPlainsBird(Bird b) {plains.add(b);}
    public void addWetlandsBird(Bird b) {wetlands.add(b);}

    public ArrayList<Bird> getForest() {return forest;}
    public ArrayList<Bird> getPlains() {return plains;}
    public ArrayList<Bird> getWetlands() {return wetlands;}

    public void runForest(Player p){
        switch (forest.size()){
            case 5: /*offer trade */;
            case 4: /*1 food */;
            case 3: /*offer trade */;
            case 2: /*1 food */;
            case 1: /*offer trade */;
            default: /*1 food */; break;
        }
        for (int i=forest.size()-1;i<=0;i--){
            Bird b = forest.get(i);
            /*b.playAbility */
        }
    }
    public void runPlains(Player p){}
    public void runWetlands(Player p){}
}

