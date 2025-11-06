package src;
import java.util.*;
public class Player {
    
    private ArrayList<Bird> cardsInHand = new ArrayList<>();
    private Bird[][] board = new Bird[3][4]; // 3 habitats with max of 4 birds in each
    private ArrayList<String> bonusCard = new ArrayList<>();
    private int[] actionCubes = new int[4];
    private ArrayList<String> foodInHand = new ArrayList<>();
    
    public Player(){
        
    }
    
    public int score(){
        return 3;
    }

    


    // Get Methods for the Player class
    public ArrayList<String> getFoodInHand(){return foodInHand;}
    public ArrayList<Bird> getCardsInHand(){return cardsInHand;}
    public ArrayList<String> getBonusCard(){return bonusCard;}
    public Bird[][] getBoard(){return board;}
    public int[] getActionCubes(){return actionCubes;}
    



    // Set Methods for the Player class
    public void addNewCardToHand(Bird b){cardsInHand.add(b);}
    public void setFoodInHand(ArrayList<String> x){foodInHand=x;}
    public void addFoodToHand(String f){foodInHand.add(f);}
    




}
