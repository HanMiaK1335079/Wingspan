package src;
import java.util.*;

public class Player {
    
    private ArrayList<Bird> cards = new ArrayList<>();
    private Board board;
    private ArrayList<Bonus> bonus = new ArrayList<>();
    private int[] actions = new int[4];
    private ArrayList<Integer> foods = new ArrayList<>();
    
    private int score = 0;
    
    public Player() {
        actions[0] = 8;
        actions[1] = 7;
        actions[2] = 6;
        actions[3] = 5;
        
        for (int i = 0; i < 5; i++) {
            foods.add(0);
        }
        
        board = new Board();
    }
    
    public int getId() {
        return 1;
    }
    
    public int getActionsRemaining() {
        if (actions.length > 0) {
            return actions[0];
        }
        return 0;
    }
    
    public void useAction() {
        if (actions.length > 0 && actions[0] > 0) {
            actions[0]--;
        }
    }
    
    public void nextRound() {
        //shorten actions by 1 since new round. No more first elem. 
        if (actions.length > 1) {
            int[] newArray = new int[actions.length - 1];
            for(int i = 0; i < newArray.length; i++) {
                int source = 1 + i;
                int dest = 0 + i;
                newArray[dest] = actions[source];
            }
            actions = newArray;
        }
    }
    
    public void resetPinkPowers() {
        ArrayList<Bird> allBirds = getAllBirdsOnBoard();
        for (Bird bird : allBirds) {
            bird.setPinkPowerUsed(false);
        }
    }
    
    public void removeFoodToken(String f) {
        switch (f) {
            case "seed" -> {
                if (foods.get(0) > 0) foods.set(0, foods.get(0) - 1);
            }
            case "fish" -> {
                if (foods.get(1) > 0) foods.set(1, foods.get(1) - 1);
            }
            case "berry" -> {
                if (foods.get(2) > 0) foods.set(2, foods.get(2) - 1);
            }
            case "insect" -> {
                if (foods.get(3) > 0) foods.set(3, foods.get(3) - 1);
            }
            case "rat" -> {
                if (foods.get(4) > 0) foods.set(4, foods.get(4) - 1);
            }
        }
    }
    
    public ArrayList<String> getFoodTokens() {
        ArrayList<String> foodList = new ArrayList<>();
        String[] types = {"seed", "fish", "berry", "insect", "rat"};
        
        for (int i = 0; i < foods.size() && i < types.length; i++) {
            int count = foods.get(i);
            for (int j = 0; j < count; j++) {
                foodList.add(types[i]);
            }
        }
        
        return foodList;
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
    
    public boolean playBird(Bird bird, String habitat) {
        if (!canPlayBird(bird, habitat)) {
            return false;
        }
        
        return board.playBird(bird, habitat);
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
        Bird[][] boardArray = board.getBoard();
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < boardArray[h].length; i++) {
                if (boardArray[h][i] != null) {
                    Bird bird = boardArray[h][i];
                    if (bird.getStoredEggs() < bird.getMaxEggs()) {
                        bird.addEggs(1);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public int getBirdCount() {
        return board.getBirdCount();
    }
    
    public int getEggCount() {
        return board.getEggCount();
    }
    
    public ArrayList<Bird> getCardsInHand() {
        return cards;
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
        return calculateScore();
    }
    
    public void addBonus(Bonus b) {
        bonus.add(b);
    }
    
    public void removeBonus(Bonus b) {
        bonus.remove(b);
    }
    
    public ArrayList<Bird> getBirdsInHabitat(String habitat) {
        switch (habitat.toLowerCase()) {
            case "forest":
            case "plains": 
            case "wetlands":
                return board.getBirdsInHabitat(habitat);
            default:
                return new ArrayList<Bird>();
        }
    }
    
    public ArrayList<Bird> getAllBirdsOnBoard() {
        return board.getAllBirds();
    }
    
    public int calculateScore() {
        return board.calculateScore();
    }
    
    public void setPlayerScore(int s) {
        score = s;
    }
    
    public ArrayList<Integer> getFoods(){return foods;}
    
    public void addFood(String f) {
        switch (f) {
            case "seed" -> foods.set(0, foods.get(0)+1);
            case "fish" -> foods.set(1, foods.get(1)+1);
            case "berry" -> foods.set(2, foods.get(2)+1);
            case "insect" -> foods.set(3, foods.get(3)+1);
            case "rat" -> foods.set(4, foods.get(4)+1);
        }
    }
    
    public void removeFood(String f) {
        switch (f) {
            case "seed" -> foods.set(0, Math.max(0, foods.get(0)-1));
            case "fish" -> foods.set(1, Math.max(0, foods.get(1)-1));
            case "berry" -> foods.set(2, Math.max(0, foods.get(2)-1));
            case "insect" -> foods.set(3, Math.max(0, foods.get(3)-1));
            case "rat" -> foods.set(4, Math.max(0, foods.get(4)-1));
        }
    }
}