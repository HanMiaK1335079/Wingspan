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
    private final List<String[]> foods;

    private BufferedImage image;
    private int storedEggs = 0;
    private int cachedFood = 0;
    private int tuckedCards = 0;
    private boolean pinkPowerUsed = false;
    private String habitat = "";

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

    private static final Map<String, Ability.Trigger> triggerMap = new HashMap<>();

    static {
        triggerMap.put("OA", Ability.Trigger.BROWN);
        triggerMap.put("WP", Ability.Trigger.WHITE);
        triggerMap.put("OBT", Ability.Trigger.PINK);
    }

    public static Bird create(String name, String abilityActivate, String abilityText, String abilityType, int points, String nest, int maxEggs, int wingspan, List<String> habitats, ArrayList<String[]> foods){
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
    public List<String[]> getFoods() { return foods; }
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

    public List<String> getDiet() {
        List<String> diet = new ArrayList<>();
        for (String[] foodArray : foods) {
            for (String food : foodArray) {
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
        String habitatNormalized = getHabitatNorm(habitat);
        return habitats != null && habitats.contains(habitatNormalized);
    }

    private String getHabitatNorm(String habitat) {
        return switch (habitat.toLowerCase(Locale.ROOT)) {
            case "forest" -> "f";
            case "plains", "grassland" -> "p";
            case "wetlands", "wetland" -> "w";
            default -> "";
        };
    }

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
    public void tuckCard(){ this.tuckedCards++; }

    public boolean triggersOnAction(ProgramState.PlayerAction action) {
        return switch (action) {
            case PLAY_BIRD -> ability.trigger == Ability.Trigger.WHITE;
            case GAIN_FOOD, LAY_EGGS, DRAW_CARDS -> ability.trigger == Ability.Trigger.BROWN;
            default -> false;
        };
    }

    public boolean isWhenPlayedAbility() {
        return ability.trigger == Ability.Trigger.WHITE;
    }

    public boolean isPinkPowerUsed() {
        return pinkPowerUsed;
    }

    public void setPinkPowerUsed(boolean used) {
        pinkPowerUsed = used;
    }

    public boolean isPredatorAbility() {
        return ability.rawText.contains("[predator]");
    }

    public int getTuckedCards() {
        return tuckedCards;
    }

    public int getCachedFood() {
        return cachedFood;
    }

    public boolean hasMaxEggs() {
        return storedEggs >= maxEggs;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Name: ").append(name).append("\n");
        s.append("Ability: ").append(ability.rawText).append("\n");
        s.append("Points: ").append(points).append("\n");
        s.append("Nest: ").append(nest).append("\n");
        s.append("Max Eggs: ").append(maxEggs).append("\n");
        s.append("Wingspan: ").append(wingspan).append("\n");
        s.append("Habitats: ").append(habitats).append("\n");
        s.append("Foods: ");
        for (String[] foodOption : foods) {
            s.append(Arrays.toString(foodOption));
        }
        s.append("\n");
        s.append("Stored Eggs: ").append(storedEggs).append("\n");
        s.append("Cached Food: ").append(cachedFood).append("\n");
        s.append("Tucked Cards: ").append(tuckedCards).append("\n");
        return s.toString();
    }

    public void resetAbility() {
        cachedFood = 0;
        tuckedCards = 0;
        pinkPowerUsed = false;
    }
}
