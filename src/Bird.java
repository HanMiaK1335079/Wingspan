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
    private ArrayList<Bird> tuckedCards = new ArrayList<>();
    private boolean pinkPowerUsed = false;

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
    public int getCachedFood() {return cachedFood;}
    public ArrayList<Bird> getTuckedCards() {return tuckedCards;}
    public boolean isPinkPowerUsed() {return pinkPowerUsed;}
    
    public void setImage(BufferedImage i) {image = i;}
    public void setPinkPowerUsed(boolean used) {pinkPowerUsed = used;}
    
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
        int seedCount = 0, fishCount = 0, berryCount = 0, insectCount = 0, ratCount = 0;
        
        for (String food : playerFoods) {
            switch (food) {
                case "seed" -> seedCount++;
                case "fish" -> fishCount++;
                case "berry" -> berryCount++;
                case "insect" -> insectCount++;
                case "rat" -> ratCount++;
            }
        }
        
        if (foods.isEmpty()) {
            return playerFoods.size() >= 1;
        }
        
        for (String[] foodSet : foods) {
            int tempSeed = seedCount;
            int tempFish = fishCount;
            int tempBerry = berryCount;
            int tempInsect = insectCount;
            int tempRat = ratCount;
            
            boolean canPay = true;
            for (String foodType : foodSet) {
                switch (foodType) {
                    case "s" -> { if (tempSeed > 0) { tempSeed--; } else { canPay = false; } }
                    case "f" -> { if (tempFish > 0) { tempFish--; } else { canPay = false; } }
                    case "b" -> { if (tempBerry > 0) { tempBerry--; } else { canPay = false; } }
                    case "i" -> { if (tempInsect > 0) { tempInsect--; } else { canPay = false; } }
                    case "r" -> { if (tempRat > 0) { tempRat--; } else { canPay = false; } }
                    case "a" -> { 
                        if (tempSeed > 0) { tempSeed--; }
                        else if (tempFish > 0) { tempFish--; }
                        else if (tempBerry > 0) { tempBerry--; }
                        else if (tempInsect > 0) { tempInsect--; }
                        else if (tempRat > 0) { tempRat--; }
                        else { canPay = false; }
                    }
                }
                if (!canPay) break;
            }
            if (canPay) return true;
        }
        return false;
    }

    public int addEggs(int eggs){ 
        if (eggs>(maxEggs-storedEggs)){
            storedEggs = maxEggs;
            return eggs-(maxEggs-storedEggs);
        }
        storedEggs += eggs;
        return 0;
    }
    
    public int removeEggs(int eggs){ 
        if (eggs>storedEggs){
            storedEggs = 0;
            return eggs-storedEggs;
        }
        storedEggs -= eggs;
        return 0;
    }

    public void cacheFood(){this.cachedFood++;}
    public void tuckCard(Bird card) {this.tuckedCards.add(card);} 

    public String toString(){
        String s = "";

        s+= "*****" + name + "*****\n";
        s+= activate+": "+"("+abilityType+") " + ability;
        s+= "\nNest: " + nest;
        s+= "\nHabitats: " + habitats;
        s+= "\nPoints: " + points;
        s+= "\nEggs: " + storedEggs + " / " + maxEggs;
        s+= "\nWingspan: "+ wingspan;
        s+= "\nCached Food: " + cachedFood; 
        s+= "\nTucked Cards: " + tuckedCards.size(); 
        s+= "\nFoods: ";

        for (String[] arr: foods){
            s+= Arrays.toString(arr)+", ";
        }

        return s;
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