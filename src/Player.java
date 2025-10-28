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



}
