package src;
import java.util.*;
public class Player {
    
    private ArrayList<Bird> cards = new ArrayList<>();
    private Bird[][] board = new Bird[3][4]; // 3 habitats with max of 4 birds in each
    private ArrayList<Bonus> bonus = new ArrayList<>();
    private int[] actions = new int[4];
    private ArrayList<String> foods = new ArrayList<>();
    
    public Player(){
        this.cards = cards;
        this.board = board;
        this.bonus = bonus;
        this.actions = actions;
        this.foods = foods;
        
    }
    
    public int score(){
        return 3;
    }

    


    // Get Methods for the Player class
    public ArrayList<String> getFoods(){return foods;}
    public ArrayList<Bird> getCards(){return cards;}
    public ArrayList<Bonus> getBonus(){return bonus;}
    public Bird[][] getBoard(){return board;}
    public int[] getActionCubes(){return actions;}
    



    // Set Methods for the Player class
    public void addCard(Bird b) {cards.add(b);}
    public void removeCard(Bird b) {cards.remove(b);}
    public void setFoodInHand(ArrayList<String> x) {foods = x;}
    public void addFood(String f) {foods.add(f);}
    public void removeFood(String f) {foods.remove(f);}
    public void addBonus(Bonus b) {bonus.add(b);}
    public void removeBonus(Bonus b) {bonus.remove(b);}





}
