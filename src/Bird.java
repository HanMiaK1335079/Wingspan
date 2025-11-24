package src;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Bird {
    private final String name;
    private final Ability ability;
    private final String abilityType;
    private final int points;
    private final String nest;
    private final int maxEggs;
    private final int wingspan;
    private final List<String> habitats;
    private final List<String[]> foods;

    private BufferedImage image;
    private int storedEggs = 0;
    private int cachedFood = 0;
    private int flocked = 0;
    private int tuckedCards = 0;

    Bird(String name, Ability ability, String abilityType, int points, String nest, int maxEggs, int wingspan, List<String> habitats, List<String[]> foods){
        this.name = name;
        this.ability = ability == null ? new Ability(Ability.Trigger.NONE, "") : ability;
        this.abilityType = abilityType == null ? "" : abilityType;
        this.points = Math.max(0, points);
        this.nest = nest == null ? "" : nest;
        this.maxEggs = Math.max(0, maxEggs);
        this.wingspan = Math.max(0, wingspan);
        this.habitats = habitats == null ? new ArrayList<>() : habitats;
        this.foods = foods == null ? new ArrayList<>() : foods;
    }

    
    public static Bird create(String name, String abilityActivate, String abilityText, String abilityType, int points, String nest, int maxEggs, int wingspan, List<String> habitats, List<String[]> foods){
        Ability.Trigger trig = Ability.Trigger.NONE;
        if (abilityActivate != null){
            switch (abilityActivate){
                case "OA" -> trig = Ability.Trigger.BROWN;
                case "WP" -> trig = Ability.Trigger.WHITE;
                case "OBT" -> trig = Ability.Trigger.PINK;
                default -> trig = Ability.Trigger.NONE;
            }
        }
        Ability ability = new Ability(trig, abilityText == null ? "" : abilityText);
        return new Bird(name, ability, abilityType, points, nest, maxEggs, wingspan, habitats, foods);
    }

    public String getName() { return name; }
    public Ability getAbility() { return ability; }
    public String getabilityType() { return abilityType; }
    public int getPoints() { return points; }
    public String getNest() { return nest; }
    public int getMaxEggs() { return maxEggs; }
    public int getWingspan() { return wingspan; }
    public List<String> getHabitats() { return habitats; }
    public List<String[]> getFoods() { return foods; }
    public BufferedImage getImage() { return image; }
    public int getStoredEggs() { return storedEggs; }

    public void setImage(BufferedImage i) { image = i; }

    public boolean canLiveInHabitat(String habitat) {
        if (habitat == null) return false;
        String habitatsn = getHabitatNorm(habitat);
        return habitats != null && habitats.contains(habitatsn);
    }

    private String getHabitatNorm(String habitat) {
        switch (habitat.toLowerCase(Locale.ROOT)) {
            case "forest": return "f";
            case "plains":
            case "grassland": return "p";
            case "wetlands":
            case "wetland": return "w";
            default: return "";
        }
    }

    public boolean canAfford(List<String> playerFoods) {
        if (playerFoods == null) return false;
        if (foods == null || foods.isEmpty()) return false;
        for (String[] option : foods){
            for (String f : option){
                if (playerFoods.contains(f)) return true;
            }
        }
        return false;
    }

    
    public int addEggs(int eggs){
        if (eggs <= 0) return 0;
        int space = maxEggs - storedEggs;
        int toAdd = Math.min(space, eggs);
        storedEggs += toAdd;
        return eggs - toAdd; // leftover
    }

    public int getEggs() {
        return storedEggs;
    }
  
    public int removeEggs(int eggs){
        if (eggs <= 0) return 0;
        int removed = Math.min(storedEggs, eggs);
        storedEggs -= removed;
        return eggs - removed;
    }

    public void cacheFood(){ this.cachedFood++; }
    public void flock(){ this.flocked++; }
    public int getTuckedCards() { return tuckedCards; }
    public void tuckCard(){ this.tuckedCards++; }
    public void untuckCard(){ if (this.tuckedCards>0) this.tuckedCards--; }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("*****").append(name).append("*****\n");
        s.append(ability).append("\n");
        s.append("Nest: ").append(nest).append('\n');
        s.append("Habitats: ").append(habitats).append('\n');
        s.append("Points: ").append(points).append('\n');
        s.append("Eggs: ").append(storedEggs).append(" / ").append(maxEggs).append('\n');
        s.append("Wingspan: ").append(wingspan).append('\n');
        s.append("Foods: ");
        if (foods != null) {
            for (String[] arr: foods){
                s.append(java.util.Arrays.toString(arr)).append(", ");
            }
        }
        return s.toString();
    }
            //THIS SHOULD WORK LATER
    // public void playAbility(){
    //     if (!abilityType.equals("N")) return;

    //     if (ability.contains("You may cache it")){
    //         /*implement the gain 1 seed thing */
    //     }else if (ability.contains("in their [wetland]")){
    //         /*implement player with fewest bird draw 1 card */
    //     }else if (ability.contains("Tuck 1")){
    //         if (ability.contains("draw 1")){
    //             /*implement draw 1 after cache */
    //         }else if (ability.contains("lay 1 egg")){
    //             /*implement lay egg after cache */
    //         }
    //     }
    // }
}