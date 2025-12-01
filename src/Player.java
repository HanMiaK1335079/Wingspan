package src;
import java.util.*;



public class Player {
    
    private String name;
    private ArrayList<Bird> cards = new ArrayList<>();
    private Board board;
    private ArrayList<Bonus> bonus = new ArrayList<>();
    private Map<Integer, Integer> actions = new HashMap<>();
    private Food food;
    private boolean needsToDiscard = false;
    private int tuckedCards = 0;
    
    private int score = 0;
    private ScoreBreakdown finalScore;
    
    public Player(String name, int food, ArrayList<Bird> startingCards, ArrayList<Bonus> startingBonuses, int id) {
        this.name = name;
        this.food = new Food();
        this.cards.addAll(startingCards);
        this.bonus.addAll(startingBonuses);
        this.id = id;
        this.board = new Board();
        for (int i = 1; i <= 4; i++) {
            actions.put(i, 8 - (i - 1));
        }
    }

    public String getName() {
        return name;
    }

    public Player() {
        actions.put(1, 8);
        actions.put(2, 7);
        actions.put(3, 6);
        actions.put(4, 5);
        
        food = new Food();
        board = new Board();
    }
    
    private int id = -1;
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public int getActionsRemaining(int round) {
        return actions.getOrDefault(round, 0);
    }
    
    public void useAction(int round) {
        if (actions.containsKey(round) && actions.get(round) > 0) {
            actions.put(round, actions.get(round) - 1);
        }
    }
    
    public ArrayList<Bird> getCardsInHand() {
        return cards;
    }
    
    public void addBonusCard(Bonus card) {
        bonus.add(card);
    }

    public String getHabitatOfBird(Bird b) {
        return board.getHabitatOfBird(b);
    }

    public boolean canDrawBonusCard() {
        return true;
    }

    public void addPoints(int points) {
        score += points;
    }

    public void nextRound() {
    }
    
    public void resetPinkPowers() {
        ArrayList<Bird> allBirds = getAllBirdsOnBoard();
        for (Bird bird : allBirds) {
            bird.setPinkPowerUsed(false);
        }
    }
    
    public void removeFood(Food.FoodType f, int amount) {
        food.removeFood(f, amount);
    }

    public Map<Food.FoodType, Integer> getPlayerFoodCounts() {
        Map<Food.FoodType, Integer> counts = new HashMap<>();
        for (Food.FoodType type : Food.FoodType.values()) {
            counts.put(type, food.getFoodCount(type));
        }
        return counts;
    }
    
    public ArrayList<Food.FoodType> getFoodTokens() {
        return food.getFoodTokens();
    }
    
    public boolean hasFoodType(Food.FoodType foodType) {
        return food.hasFood(foodType, 1);
    }
    
    public int getFoodCount(Food.FoodType foodType) {
        return food.getFoodCount(foodType);
    }
    
    public boolean playBird(Bird bird, String habitat, int position, Object... params) {
        if (!canPlayBird(bird, habitat, position)) {
            return false;
        }
        
        spendFoodForBird(bird, params);
        int eggCost = getEggCostForPlacement(habitat);
        if (!spendEggs(eggCost)) {
            return false;
        }
        return board.playBird(bird, habitat, position);
    }
    
    public boolean canPlayBird(Bird bird, String habitat, int position) {
        if (!canAffordBird(bird)) {
            return false;
        }
        int eggCost = getEggCostForPlacement(habitat);
        if (getEggCount() < eggCost) {
            return false;
        }
        return board.canPlayBird(habitat, position);
    }

    private void spendFoodForBird(Bird bird, Object... params) {
        if (bird.getFoods() == null || bird.getFoods().size() == 0) {
            return;
        }

        if (params != null && params.length > 0 && params[0] instanceof Map) {
            spendFoodFromMap((Map<Food.FoodType, Integer>) params[0]);
        } else {
            spendFirstAffordableOption(bird);
        }
    }

    private void spendFoodFromMap(Map<Food.FoodType, Integer> chosenFoods) {
        for (Map.Entry<Food.FoodType, Integer> entry : chosenFoods.entrySet()) {
            food.removeFood(entry.getKey(), entry.getValue());
        }
    }

    private void spendFirstAffordableOption(Bird bird) {
        for (Food.FoodType[] option : bird.getFoods()) {
            if (canAffordOption(option)) {
                spendFoodForOption(option);
                return;
            }
        }
    }

    public boolean canAffordBird(Bird bird) {
        if (bird.getFoods() == null || bird.getFoods().size() == 0) {
            return true;
        }
        for (Food.FoodType[] option : bird.getFoods()) {
            if (canAffordOption(option)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasFoodType(String foodType) {
        switch (foodType) {
            case "seed": return foods.get(0) > 0;
            case "fish": return foods.get(1) > 0;
            case "berry": return foods.get(2) > 0;
            case "insect": return foods.get(3) > 0;
            case "rat": return foods.get(4) > 0;
            default: return false;
        }
    }
    
    public int getFoodCount(String foodType) {
        switch (foodType) {
            case "seed": return foods.get(0);
            case "fish": return foods.get(1);
            case "berry": return foods.get(2);
            case "insect": return foods.get(3);
            case "rat": return foods.get(4);
            default: return 0;
        }
    }
    
    public boolean canPlayBird(Bird bird, String habitat) {
        
        if (!bird.canAfford(getFoodTokens())) {
            return false;
        }
        
        return board.canPlayBird(habitat);
    }

    public boolean spendEggs(int eggs) {
        if (eggs <= 0) return true;
        if (getEggCount() < eggs) return false;
        int remaining = eggs;
        Bird[][] boardArray = board.getBoard();
        for (int h = 0; h < 3 && remaining > 0; h++) {
            for (int i = 0; i < boardArray[h].length && remaining > 0; i++) {
                Bird b = boardArray[h][i];
                if (b != null) {
                    remaining = b.removeEggs(remaining);
                }
            }
        }
        return remaining == 0;
    }
    
    public boolean layEggsOnBird(Bird bird, int eggs) {
        Bird[][] boardArray = board.getBoard();
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < boardArray[h].length; i++) {
                if (boardArray[h][i] == bird) {
                    if (bird.getStoredEggs() + eggs <= bird.getMaxEggs()) {
                        bird.addEggs(eggs);
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean layEggsOnAnyBird() {
        return board.layEggsOnAnyBird();
    }

    public int getFirstEmptySlotIndex(String habitat) {
        return board.getFirstEmptySlotIndex(habitat);
    }
    
    public boolean canLayEggsOnAnyBird() {
        for (Bird bird : getAllBirdsOnBoard()) {
            if (!bird.hasMaxEggs()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Bird> getBirdsInHabitat(String habitat) {
        return board.getBirdsInHabitat(habitat);
    }
    
    public int getBirdCount() {
        return board.getBirdCount();
    }

    public int getEggCount() {
        return board.getEggCount();
    }
    
    public void setCardsInHand(ArrayList<Bird> c) {
        this.cards = c;
    }
    
    public void addCardToHand(Bird b) {
        cards.add(b);
    }
    
    public void removeCardFromHand(Bird b) {
        cards.remove(b);
    }
    
    public Bird[][] getPlayerBoard() {
        return board.getBoard();
    }
    
    public void setPlayerBoard(Bird[][] b) {
        board.setBoard(b);
    }
    
    public Board getBoard() {
        return board;
    }
    
    public ArrayList<Bonus> getBonuses() {
        return bonus;
    }
    
    public void setBonuses(ArrayList<Bonus> b) {
        this.bonus = b;
    }
    
    public int getScore(){
        return calculateScore().total();
    }
    
    public void addBonus(Bonus b) {
        bonus.add(b);
    }
    
    public void removeBonus(Bonus b) {
        bonus.remove(b);
    }
    
    public ArrayList<Bird> getAllBirdsOnBoard() {
        return board.getAllBirds();
    }
    
    public ScoreBreakdown calculateScore() {
        return board.calculateScore();
    }
    
    public void setPlayerScore(int s) {
        score = s;
    }

    public void setFinalScore(ScoreBreakdown s) {
        finalScore = s;
    }
    
    public Food getFood(){
        return food;
    }
    
    public ArrayList<Integer> getFoods() {
        ArrayList<Integer> foodCounts = new ArrayList<>();
        for (Food.FoodType type : Food.FoodType.values()) {
            if(type != Food.FoodType.SEED_INSECT)
                foodCounts.add(food.getFoodCount(type));
        }
        return foodCounts;
    }
    
    public void addFood(Food.FoodType foodType, int amount) {
        food.addFood(foodType, amount);
    }

    public void tuckCard() {
        tuckedCards++;
    }
    
    public ArrayList<Bird> getBirdsByNestType(String nestType) {
        return board.getBirdsByNestType(nestType);
    }
    
    public boolean hasBirdWithNestType(String nestType) {
        ArrayList<Bird> birds = getBirdsByNestType(nestType);
        return !birds.isEmpty();
    }
    
    public Bird getRightmostBirdInHabitat(String habitat) {
        return board.getRightmostBirdInHabitat(habitat);
    }

    public boolean isHabitatFull(String habitat) {
        return board.isHabitatFull(habitat);
    }

    public boolean isBirdRightmostInHabitat(Bird b, String habitat) {
        return board.isBirdRightmostInHabitat(b, habitat);
    }

    public void layEggsInNestType(String nestType) {
        board.layEggsInNestType(nestType);
    }

    public void setNeedsToDiscard(boolean needsToDiscard) {
        this.needsToDiscard = needsToDiscard;
    }

    public boolean getNeedsToDiscard() {
        return needsToDiscard;
    }
    
    public boolean canAffordFood(Food.FoodType f, int amount) {
        return food.hasFood(f, amount);
    }
    
    public void spendFood(Food.FoodType f, int amount) {
        food.removeFood(f, amount);
    }
}