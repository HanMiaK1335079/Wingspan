package src;

import java.awt.image.BufferedImage;
import java.util.*;


public class Bird {

    public static Map<String, String> getFoodNameMap() {
        return foodNameMap;
    }
    private final String name;
    private final Ability ability;
    private final String abilityType;
    private final int points;
    private final String nest;
    private final int maxEggs;
    private final int wingspan;
    private final List<String> habitats;
    private final List<Food.FoodType[]> foods;

    private BufferedImage image;
    private int storedEggs = 0;
    private int cachedFood = 0;
    private int flocked = 0;
    private int tuckedCards = 0;
    private boolean pinkPowerUsed = false;

    Bird(String name, Ability ability, String abilityType, int points, String nest, int maxEggs, int wingspan, List<String> habitats, List<Food.FoodType[]> foods){
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

    private static final Map<String, Ability.Trigger> triggerMap = new HashMap<>();

    static {
        triggerMap.put("OA", Ability.Trigger.BROWN);
        triggerMap.put("WP", Ability.Trigger.WHITE);
        triggerMap.put("OBT", Ability.Trigger.PINK);
    }

    public static Bird create(String name, String abilityActivate, String abilityText, String abilityType, int points, String nest, int maxEggs, int wingspan, List<String> habitats, List<Food.FoodType[]> foods){
        Ability.Trigger trig = triggerMap.getOrDefault(abilityActivate, Ability.Trigger.NONE);
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
    public List<Food.FoodType[]> getFoods() { return foods; }
    public BufferedImage getImage() { return image; }
    public int getStoredEggs() { return storedEggs; }

    public int getFoodCost() {
        if (foods.isEmpty()) {
            return 0;
        }
        return foods.get(0).length;
    }

    public int getEggCount() {
        return storedEggs;
    }

    public List<Food.FoodType> getDiet() {
        List<Food.FoodType> diet = new ArrayList<>();
        for (Food.FoodType[] foodArray : foods) {
            for (Food.FoodType food : foodArray) {
                if (!diet.contains(food)) {
                    diet.add(food);
                }
            }
        }
        return diet;
    }

    public String getAbilityText() {
        return ability.rawText;
    }

    public boolean isRollDieAbility() {
        return ability.rawText.contains("[roll die]");
    }

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

    //some like memory leak format preference for vscode idk
    private static final Map<String, String> foodNameMap = Map.ofEntries(
        Map.entry("i", "insect"),
        Map.entry("s", "seed"),
        Map.entry("f", "fish"),
        Map.entry("b", "berry"),
        Map.entry("r", "rat"),
        Map.entry("a", "wild"),
        Map.entry("wild", "wild"),
        Map.entry("insect", "insect"),
        Map.entry("seed", "seed"),
        Map.entry("fish", "fish"),
        Map.entry("berry", "berry"),
        Map.entry("rat", "rat")
    );

    private String convertFoodNames(String food) {
        if (food == null) return null;
        return foodNameMap.get(food.toLowerCase(Locale.ROOT));
    }

    public boolean canAfford(Food playerFood) {
        if (foods == null || foods.isEmpty()) {
            return true;
        }

        for (Food.FoodType[] option : foods) {
            if (isOptionAffordable(playerFood, option)) {
                return true;
            }
        }

        return false;
    }

    private boolean isOptionAffordable(Food playerFood, Food.FoodType[] costOption) {
        Map<Food.FoodType, Integer> requiredCounts = new HashMap<>();
        int wildcardsRequired = 0;

        for (Food.FoodType foodCost : costOption) {
            if (foodCost == null) continue;

            requiredCounts.put(foodCost, requiredCounts.getOrDefault(foodCost, 0) + 1);
        }

        for (Map.Entry<Food.FoodType, Integer> requirement : requiredCounts.entrySet()) {
            Food.FoodType foodType = requirement.getKey();
            int requiredAmount = requirement.getValue();
            int availableAmount = playerFood.getFoodCount(foodType);

            if (availableAmount < requiredAmount) {
                return false; 
            }
            playerFood.removeFood(foodType, requiredAmount);
        }

        int remainingFood = playerFood.getFoodTokens().size();
        
        return remainingFood >= wildcardsRequired;
    }

    
    public int addEggs(int eggs){
        if (eggs <= 0) return 0;
        int space = maxEggs - storedEggs;
        int toAdd = Math.min(space, eggs);
        storedEggs += toAdd;
        return eggs - toAdd; // leftover
    }

    public int removeEggs(int eggs){
        if (eggs <= 0) return 0;
        int removed = Math.min(storedEggs, eggs);
        storedEggs -= removed;
        return eggs - removed;
    }

    public void cacheFood(){ this.cachedFood++; }
    public void flock(){ this.flocked++; }
    public void tuckCard(){ this.tuckedCards++; }

    public boolean triggersOnAction(ProgramState.PlayerAction action) {
        switch (action) {
            case PLAY_BIRD:
                return ability.trigger == Ability.Trigger.WHITE;
            case GAIN_FOOD:
            case LAY_EGGS:
            case DRAW_CARDS:
                return ability.trigger == Ability.Trigger.BROWN;
            default:
                return false;
        }
    }

    public boolean isWhenPlayedAbility() {
        return ability.trigger == Ability.Trigger.WHITE;
    }

    public boolean isPinkPowerUsed() {
        return pinkPowerUsed;
    }

    public void setPinkPowerUsed(boolean used) {
        this.pinkPowerUsed = used;
    }

    public int getTuckedCards() {
        return tuckedCards;
    }

    public void resetPinkPower() {
        this.pinkPowerUsed = false;
    }

    public int getCachedFood() { 
        return cachedFood; 
    }

    public String getHabitat() {
        if (habitats.isEmpty()) {
            return "";
        }
        return habitats.get(0);
    }

    public void setHabitat(String habitat) {
        if (!habitats.contains(habitat)) {
            habitats.add(habitat);
        }
    }

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
            for (Food.FoodType[] arr: foods){
                s.append(java.util.Arrays.toString(arr)).append(", ");
            }
        }
        return s.toString();
    }

    public boolean isCachedFoodAbility() {
        return ability.mentionsCache();
    }
    
    public boolean isTuckAbility() {
        return ability.mentionsTuck();
    }
    
    public boolean isDrawAbility() {
        return ability.mentionsDraw();
    }
    
    public boolean isLayEggsAbility() {
        return ability.mentionsLayEgg();
    }
    
    public boolean isGainFoodAbility() {
        return ability.isGainFoodAbility();
    }
    
    public boolean isPredatorAbility() {
        return abilityType != null && abilityType.equals("predator");
    }
    
    public int getPredatorWingspan() {
        //place holder .Actual implementation relies on birdInfo.csv
        return 100;
    }
    
    public boolean isWhenActivatedAbility() {
        return ability.trigger == Ability.Trigger.BROWN;
    }
    
    public boolean isWhenOtherPlayerAbility() {
        return ability.trigger == Ability.Trigger.PINK;
    }
    
    public boolean isAllPlayersAbility() {
        return ability.isAllPlayersAbility();
    }
    
    public boolean isPlayerWithFewestAbility() {
        return ability.isPlayerWithFewestAbility();
    }
    
    public void layEgg() {
        if (storedEggs < maxEggs) {
            storedEggs++;
        }
    }

    public boolean hasMaxEggs() {
        return storedEggs >= maxEggs;
    }
}