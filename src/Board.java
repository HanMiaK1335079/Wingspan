package src;
import java.util.*;
import static java.lang.System.*;

public class Board {
    public ArrayList<Bird> forest = new ArrayList<Bird>();
    public ArrayList<Bird> plains = new ArrayList<Bird>();
    public ArrayList<Bird> wetlands = new ArrayList<Bird>();
    
    public void addBird(String habitat, Bird b){
        switch (habitat){
            case ("forest") -> forest.add(b);
            case ("plains") -> plains.add(b);
            case ("wetlands") -> wetlands.add(b);
            default -> out.println(habitat + " is not valid");
        }
    }
    public void addForestBird(Bird b) {forest.add(b);}
    public void addPlainsBird(Bird b) {plains.add(b);}
    public void addWetlandsBird(Bird b) {wetlands.add(b);}

    public ArrayList<Bird> getHabitat (String h){
        return switch (h) {
            case ("forest") -> getForest();
            case ("plains") -> getPlains();
            default -> getWetlands();
        };
    }
    public ArrayList<Bird> getForest() {return forest;}
    public ArrayList<Bird> getPlains() {return plains;}
    public ArrayList<Bird> getWetlands() {return wetlands;}

    public void playHabitat(Player p, String h){
        switch (h){
            case ("forest") -> runForest(p);
            case ("plains") -> runPlains(p);
            default -> runWetlands(p);
        }
    }
    public void runForest(Player p){
        switch (forest.size()){
            case 5: /*offer trade */;
            case 4: /*1 food */;
            case 3: /*offer trade */;
            case 2: /*1 food */;
            case 1: /*offer trade */;
            default: /*1 food */; break;
        }
    }
    public void runPlains(Player p){
        switch (forest.size()){
            case 5: /*offer trade */;
            case 4: /*1 egg */;
            case 3: /*offer trade */;
            case 2: /*1 egg */;
            case 1: /*offer trade */;
            default: /*1 egg */; break;
        }
    }
    public void runWetlands(Player p){
        switch (forest.size()){
            case 5: /*offer trade */;
            case 4: /*1 birb */;
            case 3: /*offer trade */;
            case 2: /*1 birb */;
            case 1: /*offer trade */;
            default: /*1 birb */; break;
        }
    }
}

