package src;
import java.util.*;



public class Player {
    
    private String name;
    private ArrayList<Bird> cards = new ArrayList<>();
    private Board board;
    private ArrayList<Bonus> bonus = new ArrayList<>();
    private Map<Integer, Integer> actions = new HashMap<>();
    private ArrayList<Integer> foods = new ArrayList<>();
    private boolean needsToDiscard = false;
    private int tuckedCards = 0;
    private int seedNum=0;
    private int fishNum=0;
    private int berryNum=0;
    private int invertebrateNum=0;
    private int rodentNum=0;
    
    private int score = 0;
    private ScoreBreakdown finalScore;
    
    public Player(String name, ArrayList<Bird> startingCards, ArrayList<Bonus> startingBonuses, int id) {
        this.name = name;
        this.foods = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
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
        
        this.foods = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));
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

    public boolean hasFood(){
        for (int x: foods){
            if (x>0) return true;
        }
        return false;
    }
    
    public void removeFood(String foodType, int amount) {
        switch (foodType) {
            case "s" -> foods.set(0, Math.max(0, foods.get(0) - amount));
            case "f" -> foods.set(1, Math.max(0, foods.get(1) - amount));
            case "b" -> foods.set(2, Math.max(0, foods.get(2) - amount));
            case "i" -> foods.set(3, Math.max(0, foods.get(3) - amount));
            case "r" -> foods.set(4, Math.max(0, foods.get(4) - amount));
        }
    }

    public Map<String, Integer> getPlayerFoodCounts() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("s", foods.get(0));
        counts.put("f", foods.get(1));
        counts.put("b", foods.get(2));
        counts.put("i", foods.get(3));
        counts.put("r", foods.get(4));
        return counts;
    }
    
    public ArrayList<String> getFoodTokens() {
        ArrayList<String> foodList = new ArrayList<>();
        String[] types = {"s", "f", "b", "i", "r"};
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < foods.get(i); j++) {
                foodList.add(types[i]);
            }
        }
        return foodList;
    }
    
    public boolean hasFoodType(String foodType) {
        return getFoodCount(foodType) >= 1;
    }
    
    public int getFoodCount(String foodType) {
        switch (foodType) {
            case "s" -> { return foods.get(0); }
            case "f" -> { return foods.get(1); }
            case "b" -> { return foods.get(2); }
            case "i" -> { return foods.get(3); }
            case "r" -> { return foods.get(4); }
            default -> { return 0; }
        }
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

    @SuppressWarnings("unchecked")
    private void spendFoodForBird(Bird bird, Object... params) {
        if (bird.getFoods() == null || bird.getFoods().size() == 0) {
            return;
        }

        if (params != null && params.length > 0 && params[0] instanceof Map) {
            spendFoodFromMap((Map<String, Integer>) params[0]);
        } else {
            spendFirstAffordableOption(bird);
        }
    }

    private void spendFoodFromMap(Map<String, Integer> chosenFoods) {
        for (Map.Entry<String, Integer> entry : chosenFoods.entrySet()) {
            removeFood(entry.getKey(), entry.getValue());
        }
    }

    private void spendFirstAffordableOption(Bird bird) {
        for (String[] option : bird.getFoods()) {
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
        for (String[] option : bird.getFoods()) {
            if (canAffordOption(option)) {
                return true;
            }
        }
        return false;
    }

    private boolean canAffordOption(String[] option) {
        Map<String, Integer> requiredFoodCounts = new HashMap<>();
        int wildRequired = 0;
        for (String food : option) {
            if (food.equals("a")) {
                wildRequired++;
            } else {
                requiredFoodCounts.put(food, requiredFoodCounts.getOrDefault(food, 0) + 1);
            }
        }

        Map<String, Integer> playerFoodCounts = getPlayerFoodCounts();
        for (Map.Entry<String, Integer> entry : requiredFoodCounts.entrySet()) {
            if (playerFoodCounts.getOrDefault(entry.getKey(), 0) < entry.getValue()) {
                return false;
            }
        }

        int leftovers = 0;
        for (String t : new String[]{"s", "f", "b", "i", "r"}) {
            leftovers += Math.max(0, playerFoodCounts.getOrDefault(t, 0) - requiredFoodCounts.getOrDefault(t, 0));
        }

        return leftovers >= wildRequired;
    }

    private void spendFoodForOption(String[] option) {
        Map<String, Integer> requiredFoodCounts = new HashMap<>();
        int wildRequired = 0;
        for (String food : option) {
            if (food.equals("a")) {
                wildRequired++;
            } else {
                requiredFoodCounts.put(food, requiredFoodCounts.getOrDefault(food, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : requiredFoodCounts.entrySet()) {
            removeFood(entry.getKey(), entry.getValue());
        }
        for (int i = 0; i < wildRequired; i++) {
            spendAnyFood();
        }
    }

    private void spendAnyFood() {
        for (String t : new String[]{"s", "f", "b", "i", "r"}) {
            if (hasFoodType(t)) {
                removeFood(t, 1);
                break;
            }
        }
    }

    private int getEggCostForPlacement(String habitat) {
        switch(board.getBirdsInHabitat(habitat).size()) {
            case 0 -> { return 0; }
            case 1 -> { return 1; }
            case 2 -> { return 1; }
            case 3 -> { return 2; }
            case 4 -> { return 2;}
            
        }
        return 0;
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
    
    public ArrayList<Integer> getFood(){
        return foods;
    }
    
    public ArrayList<Integer> getFoods() {
        return new ArrayList<>(foods);
    }
    
    public void addFood(String foodType, int amount) {
        switch (foodType) {
            case "s" -> foods.set(0, foods.get(0) + amount);
            case "f" -> foods.set(1, foods.get(1) + amount);
            case "b" -> foods.set(2, foods.get(2) + amount);
            case "i" -> foods.set(3, foods.get(3) + amount);
            case "r" -> foods.set(4, foods.get(4) + amount);
        }
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
    
    public boolean canAffordFood(String f, int amount) {
        return getFoodCount(f) >= amount;
    }
    
    public void spendFood(String f, int amount) {
        removeFood(f, amount);
    }
    public boolean canLayEggs() {
        return canLayEggsOnAnyBird();
    }
}