package src;
import java.util.*;
public class Player {
    
    private ArrayList<Bird> cards = new ArrayList<>();
    private Bird[][] board = new Bird[3][4]; // 3 habitats with max of 4 birds in each
    private ArrayList<Bonus> bonus = new ArrayList<>();
    private int[] actions = new int[4];
    private ArrayList<Integer> foods = new ArrayList<>();
    
    public Player(){
        this.cards = cards;
        this.board = board;
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
    public Bird[][] getBoard(){return board;}
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





}
