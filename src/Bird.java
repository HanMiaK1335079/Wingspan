<<<<<<< HEAD
import java.awt.image.BufferedImage;

public class Bird {
    final String name;
    final String[] habitats;
    final String[] foods;
    final int feathers;
    final String nest;
    final int maxEggs;
    final int wingspan;
    final String ability;
    final BufferedImage image;
    //final String abilityClass; // returns onActivate, betweenTurns, whenPlaced
    //final String abilityType; // returns hunt, flock, none

    int storedEggs = 0;
    int cachedFood = 0;
    int flocked = 0;

    public Bird(String n, String[] h, String[] f, int fo, String ne, int mE, int w, String a, BufferedImage i){
=======
package src;
import java.util.*;

public class Bird {
    final private String name; // name of the bird
    final private String[] habitats; // where the bird can live
    final private String[] foods; // what the bird can eat
    final private int feathers; // number of feathers the bird has
    final private String nest; // type of nest the bird uses
    final private int maxEggs; // maximum number of eggs the bird can hold
    final private int wingspan; // wingspan of the bird in cm
    final private String ability; //**** HOW WOULD THIS BE IMPLEMENTED?
    final private String[] foodRequired; //stores all diffrent combinations of food required to play the bird

    private int storedEggs = 0;
    private ArrayList<String> cachedFood = new ArrayList<>();
    private ArrayList<Bird> flocked = new ArrayList<>();

    public Bird(String n, String[] h, String[] f, int fo, int mE, int w, String ne, String a, String[] fr){
>>>>>>> 739a3b504828b762678dbc313c4eabf69250ee5e
        this.name = n;
        this.habitats = h;
        this.foods = f;
        this.feathers = fo;
        this.nest = ne;
        this.maxEggs = mE;
        this.wingspan = w;
        this.ability = a;
<<<<<<< HEAD
        this.image = i;
    }

    public String getName() {return this.name;}
    public String[] getHabitats() {return this.habitats;}
    public String[] getFoods() {return this.foods;}
    public int getFeathers() {return this.feathers;}
    public String getNest() {return this.nest;}
    public int getMaxEggs() {return this.maxEggs;}
    public int getWingspan() {return this.wingspan;}
    public String getAbility() {return this.ability;}
    public int getStoredEggs() {return this.storedEggs;}
    public int getCachedFood() {return this.cachedFood;}
    public int getFlocked() {return this.flocked;}
    public BufferedImage getImage() {return this.image;}
=======
        this.foodRequired = fr;
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
    public int getStoredEggs() {return storedEggs;}
    public ArrayList<String> getCachedFood() {return cachedFood;}
    public ArrayList<Bird> getFlocked() {return flocked;}
>>>>>>> 739a3b504828b762678dbc313c4eabf69250ee5e

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
}

    

