package src;
import java.awt.image.BufferedImage;
import java.util.*;
public class Bird {
    final private String name; // name of the bird
    final private String[] habitats; // where the bird can live
    final private String[] foods; // what the bird can eat
    final private int feathers; // number of feathers the bird has
    final private String nest; // type of nest the bird uses
    final private int maxEggs; // maximum number of eggs the bird can hold
    final private int wingspan; // wingspan of the bird in cm
    final private String ability; //**** HOW WOULD THIS BE IMPLEMENTED? WHO KNOW?!!?
    final private String[] foodRequired; //stores all diffrent combinations of food required to play the bird
    private final BufferedImage image;

    private int storedEggs = 0;
    private ArrayList<String> cachedFood = new ArrayList<>();
    private ArrayList<Bird> flocked = new ArrayList<>();

    public Bird(String n, String[] h, String[] f, int fo, String ne, int mE, int w, String a, String[] fr, BufferedImage img){
        this.name = n;
        this.habitats = h;
        this.foods = f;
        this.feathers = fo;
        this.nest = ne;
        this.maxEggs = mE;
        this.wingspan = w;
        this.ability = a;
        this.foodRequired = fr;
        this.image = img;
    }

    public String getName() {return name;}
    public String[] getHabitats() {return habitats;}
    public String[] getFoods() {return foods;}
    public int getFeathers() {return feathers;}
    public String getNest() {return nest;}
    public int getMaxEggs() {return maxEggs;}
    public int getWingspan() {return wingspan;}
    public String getAbility() {return ability;}
    public String[] getFoodRequired() {return foodRequired;}
    public BufferedImage getImage() {return image;}
    public int getStoredEggs() {return storedEggs;}
    public ArrayList<String> getCachedFood() {return cachedFood;}
    public ArrayList<Bird> getFlocked() {return flocked;}

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

    public void cacheFood(){
        cachedFood.add("wild");
    }
    public void cacheFood(String foodType){
        if (foodType != null && !foodType.isEmpty()) {
            cachedFood.add(foodType);
        }
    }
    public void flock(Bird other){
        if (other != null) {
            flocked.add(other);
        }
    }
}

    

