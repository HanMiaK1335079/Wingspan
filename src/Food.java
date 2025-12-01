package src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Food {
    public enum FoodType {
        SEED("seed"),
        FISH("fish"),
        BERRY("berry"),
        INSECT("insect"),
        RAT("rat"),
        SEED_INSECT("seed insect");

        private final String name;

        FoodType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static FoodType fromString(String text) {
            for (FoodType b : FoodType.values()) {
                if (b.name.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    private Map<FoodType, Integer> foodCounts = new HashMap<>();

    public Food() {
        for (FoodType foodType : FoodType.values()) {
            if (foodType != FoodType.SEED_INSECT) {
                foodCounts.put(foodType, 0);
            }
        }
    }

    public int getFoodCount(FoodType foodType) {
        return foodCounts.getOrDefault(foodType, 0);
    }

    public void addFood(FoodType foodType, int amount) {
        foodCounts.put(foodType, getFoodCount(foodType) + amount);
    }

    public void removeFood(FoodType foodType, int amount) {
        foodCounts.put(foodType, Math.max(0, getFoodCount(foodType) - amount));
    }

    public boolean hasFood(FoodType foodType, int amount) {
        return getFoodCount(foodType) >= amount;
    }

    public ArrayList<FoodType> getFoodTokens() {
        ArrayList<FoodType> foodList = new ArrayList<>();
        for (Map.Entry<FoodType, Integer> entry : foodCounts.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                foodList.add(entry.getKey());
            }
        }
        return foodList;
    }

    public Map<FoodType, Integer> getFoodCounts() {
        return foodCounts;
    }
}