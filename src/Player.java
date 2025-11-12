package src;
import java.util.*;
public class Player {
    
    private ArrayList<Bird> cards = new ArrayList<>();
    private Bird[][] board = new Bird[3][4]; // 3 habitats with max of 4 birds in each
    private ArrayList<Bonus> bonus = new ArrayList<>();
    private int[] actions = new int[4];
    private ArrayList<String> foods = new ArrayList<>();
    private int playerScore = 0;
    
    public Player() {
        actions[0] = 8;
        actions[1] = 7;
        actions[2] = 6;
        actions[3] = 5;
    }
    
    public int score(){
        return playerScore;
    }

    
    public int getPlayerId() {
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
        if (actions.length > 1) {
            int[] newArray = new int[actions.length - 1];
            System.arraycopy(actions, 1, newArray, 0, newArray.length);
            actions = newArray;
        }
    }
    
    public ArrayList<String> getFoodTokens() {
        return foods;
    }
    
    public void setFoodTokens(ArrayList<String> foods) {
        this.foods = foods;
    }
    
    public void addFoodToken(String f) {
        foods.add(f);
    }
    
    public void removeFoodToken(String f) {
        foods.remove(f);
    }
    
    public ArrayList<Bird> getCardsInHand() {
        return cards;
    }
    
    public void setCardsInHand(ArrayList<Bird> cards) {
        this.cards = cards;
    }
    
    public void addCardToHand(Bird b) {
        cards.add(b);
    }
    
    public void removeCardFromHand(Bird b) {
        cards.remove(b);
    }
    
    public Bird[][] getPlayerBoard() {
        return board;
    }
    
    public void setPlayerBoard(Bird[][] board) {
        this.board = board;
    }
    
    public ArrayList<Bonus> getPlayerBonuses() {
        return bonus;
    }
    
    public void setPlayerBonuses(ArrayList<Bonus> bonus) {
        this.bonus = bonus;
    }
    
    public void addPlayerBonus(Bonus b) {
        bonus.add(b);
    }
    
    public void removePlayerBonus(Bonus b) {
        bonus.remove(b);
    }
    
    public boolean playBird(Bird bird, String habitat) {
        int habitatIndex = getHabitatIndex(habitat);
        if (habitatIndex == -1) return false;
        
        for (int i = 0; i < board[habitatIndex].length; i++) {
            if (board[habitatIndex][i] == null) {
                board[habitatIndex][i] = bird;
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Bird> getBirdsInHabitat(String habitat) {
        int habitatIndex = getHabitatIndex(habitat);
        if (habitatIndex == -1) return new ArrayList<>();
        
        ArrayList<Bird> birds = new ArrayList<>();
        for (int i = 0; i < board[habitatIndex].length; i++) {
            if (board[habitatIndex][i] != null) {
                birds.add(board[habitatIndex][i]);
            }
        }
        return birds;
    }
    
    private int getHabitatIndex(String habitat) {
        switch (habitat.toLowerCase()) {
            case "forest": return 0;
            case "plains": return 1;
            case "wetlands": return 2;
            default: return -1;
        }
    }
    
    public int calculateScore() {
        int totalScore = playerScore;
        
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    totalScore += board[h][i].getPoints();
                }
            }
        }
        
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    totalScore += board[h][i].getStoredEggs();
                }
            }
        }
        
        for (Bonus b : bonus) {
            totalScore += 5;
        }
        
        return totalScore;
    }
    
    public void setPlayerScore(int score) {
        playerScore = score;
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
    public void addFood(String f) {foods.add(f);}
    public void removeFood(String f) {foods.remove(f);}
    public void addBonus(Bonus b) {bonus.add(b);}
    public void removeBonus(Bonus b) {bonus.remove(b);}





}
