package src;
import java.util.*;
public class Player {
    
    private ArrayList<Bird> cards = new ArrayList<>();
    private ArrayList<Bonus> bonus = new ArrayList<>();
    private int[] actions = new int[4];
    private ArrayList<Integer> foods = new ArrayList<>();
    private int score;
    
    public Player(){
        this.cards = cards;
        this.bonus = bonus;
        this.actions = actions;
        for (int i=0;i<5;i++) foods.add(0);
        
    }
    
    public int score(){
        return 3;
    }

    


    // Get Methods for the Player class
    public ArrayList<Integer> getFoods(){return foods;}
    public ArrayList<Bird> getCards(){return cards;}
    public ArrayList<Bonus> getBonus(){return bonus;}
    public int[] getActionCubes(){return actions;}
    



    // Set Methods for the Player class
    public void addCard(Bird b) {cards.add(b);}
    public void removeCard(Bird b) {cards.remove(b);}
    public void addFood(String f) {
        switch (f) {
            case "seed" -> foods.set(0, foods.get(0)+1);
            case "fish" -> foods.set(1, foods.get(1)+1);
            case "berry" -> foods.set(2, foods.get(2)+1);
            case "insect" -> foods.set(3, foods.get(3)+1);
            case "rat" -> foods.set(4, foods.get(4)+1);
        }
    }
    public void removeFood(String f) {foods.remove(f);}
    public void addBonus(Bonus b) {bonus.add(b);}
    public void removeBonus(Bonus b) {bonus.remove(b);}

    public int calculateScore(){
        score = 0;
        for (Bird b : cards){
            score += b.getPoints();
            score += b.getStoredEggs();
        }
        for (Bonus b : bonus){
            score += b.calculateBonus(this);
        }
        return score;
    }

    public void nextRound() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'nextRound'");
    }

    public void setPlayerScore(int calculateScore) {
        score = calculateScore;
    }

    public int getActionsRemaining() {
        return actions[3];
    }

    public void useAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'useAction'");
    }

    public boolean playBird(Bird bird, String habitat) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'playBird'");
    }



}