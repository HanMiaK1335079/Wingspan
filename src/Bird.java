package src;
import java.util.*;

public class Bird {
    final String name;
    final String[] habitats;
    final String[] foods;
    final int feathers;
    final String nest;
    final int maxEggs;
    final int wingspan;
    final String ability;

    int storedEggs = 0;
    ArrayList<String> cachedFood = new ArrayList<>();
    ArrayList<Bird> flocked = new ArrayList<>();

    public Bird(String n, String[] h, String[] f, int fo, String ne, int mE, int w, String a){
        this.name = n;
        this.habitats = h;
        this.foods = f;
        this.feathers = fo;
        this.nest = ne;
        this.maxEggs = mE;
        this.wingspan = w;
        this.ability = a;
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
    public ArrayList<String> getCachedFood() {return this.cachedFood;}
    public ArrayList<Bird> getFlocked() {return this.flocked;}

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

    public void cacheFood(String food){this.cachedFood.add(food);}
    public void cacheFood(String[] food){this.cachedFood.addAll(Arrays.asList(food));}

    public void flock(Bird b){this.flocked.add(b);}
    public void flock(Bird[] birds){this.flocked.addAll(Arrays.asList(birds));
}

    
}
