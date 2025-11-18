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
    
    public void removeFoodToken(String f) {
        foods.remove(f);
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
    
    public ArrayList<Bonus> getBonuses() {
        return bonus;
    }
    
    public void setBonuses(ArrayList<Bonus> b) {
        this.bonus = b;
        for (int i=0;i<5;i++) foods.set(i, 0);
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
    
    public boolean playBird(Bird bird, String habitat) {
        return board.playBird(bird, habitat);
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