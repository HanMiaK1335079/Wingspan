package src;
import java.awt.image.BufferedImage;
import java.util.*;
public class Bird {
    final private String name;
    final private String activate;
    final private String ability;
    final private String abilityType;
    final private int points;
    final private String nest;
    final private int maxEggs;
    final private int wingspan;
    final private ArrayList<String> habitats;
    final private ArrayList<String[]> foods;

    private BufferedImage image;
    private int storedEggs = 0;
    private int cachedFood = 0;
    private int flocked = 0;

    public Bird(String n, String a, String ab, String aT, int p, String ne, int m, int w, ArrayList<String> h, ArrayList<String[]> f){
        name = n;
        activate = a;
        ability = ab;
        abilityType = aT;
        points = p;
        nest = ne;
        maxEggs = m;
        wingspan = w;
        habitats = h;
        foods = f;
    }

    public String getName() {return name;}
    public String getActivate() {return activate;}
    public String getAbility() {return ability;}
    public String getabilityType() {return abilityType;}
    public int getPoints() {return points;}
    public String getNest() {return nest;}
    public int getMaxEggs() {return maxEggs;}
    public int getWingspan() {return wingspan;}
    public ArrayList<String> getHabitats() {return habitats;}
    public ArrayList<String[]> getFoods() {return foods;}
    public BufferedImage getImage() {return image;}
    public int getStoredEggs() {return storedEggs;}

    public void setImage(BufferedImage i) {image = i;}
    
    public boolean canLiveInHabitat(String habitat) {
        String habitatsn = getHabitatNorm(habitat);
        return habitats.contains(habitatsn);
    }
    
    private String getHabitatNorm(String habitat) {
        switch (habitat.toLowerCase()) {
            case "forest": return "f";
            case "plains": 
            case "grassland": return "p";
            case "wetlands": 
            case "wetland": return "w";
            default: return "";
        }
    }
    
    public boolean canAfford(ArrayList<String> playerFoods) {
        return playerFoods.size() >= 1;
    }

    public int addEggs(int eggs){ //adds eggs to bird and returns unadded eggs
        if (eggs>(maxEggs-storedEggs)){
            storedEggs = maxEggs;
            return eggs-(maxEggs-storedEggs);
        }
        storedEggs += eggs;
        return 0;
    }
    
    public int removeEggs(int eggs){ // removes eggs and returns eggs still needed to be removed
        if (eggs>storedEggs){
            storedEggs = 0;
            return eggs-storedEggs;
        }
        storedEggs -= eggs;
        return 0;
    }

    public void cacheFood(){this.cachedFood++;}
    public void flock(){this.flocked++;}

    public String toString(){
        String s = "";

        s+= "*****" + name + "*****\n";
        s+= activate+": "+"("+abilityType+") " + ability;
        s+= "\nNest: " + nest;
        s+= "\nHabitats: " + habitats;
        s+= "\nPoints: " + points;
        s+= "\nEggs: " + storedEggs + " / " + maxEggs;
        s+= "\nWingspan: "+ wingspan;
        s+= "\nFoods: ";

        for (String[] arr: foods){
            s+= Arrays.toString(arr)+", ";
        }

        return s;
    }

    public int getScore(){
        return points + storedEggs + cachedFood + flocked;
    }

    public void playAbility(){
        if (!abilityType.equals("brown")) return;

        if (ability.contains("You may cache it")){
            /*implement the gain 1 seed thing */
        }else if (ability.contains("in their [wetland]")){
            /*implement player with fewest bird draw 1 card */
        }else if (ability.contains("Tuck 1")){
            if (ability.contains("draw 1")){
                /*implement draw 1 after cache */
            }else if (ability.contains("lay 1 egg")){
                /*implement lay egg after cache */
            }
        }
    }
}