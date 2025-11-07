package src;
import java.util.*;
public class Player {
    // Cards and board
    private ArrayList<Bird> cardsInHand = new ArrayList<>();
    private Bird[][] board = new Bird[3][4]; // 3 habitats with max of 4 birds in each
    private ArrayList<String> bonusCard = new ArrayList<>();
    private int[] actionCubes = new int[4];
    private ArrayList<String> foodInHand = new ArrayList<>();

    // Scoring fields
    private int birdCardPoints = 0;
    private int bonusCardPoints = 0;
    private int endOfRoundGoalPoints = 0;
    private int eggCount = 0;
    private int cachedFoodCount = 0;
    private int tuckedCardCount = 0;
    private int unusedFoodTokens = 0;
    private int totalScore = 0;


    
    public Player(){
        
    }
    
    // Calculate total score at game end
    public int calculateTotalScore() {
        // Bird card points: sum printed points on all birds on board
        birdCardPoints = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Bird b = board[i][j];
                if (b != null) {
                    birdCardPoints += b.getPoints();
                    eggCount += b.getEggCount();
                    cachedFoodCount += b.getCachedFoodCount();
                    tuckedCardCount += b.getTuckedCardCount();
                }
            }
        }
        // Bonus card points: sum printed points on bonus cards
        // (Assume bonusCard stores card names, you may need to look up points elsewhere)
        // For now, just use bonusCardPoints field
        // End-of-round goals: use endOfRoundGoalPoints field
        // Unused food tokens: use unusedFoodTokens field
        totalScore = birdCardPoints + bonusCardPoints + endOfRoundGoalPoints
            + eggCount + cachedFoodCount + tuckedCardCount;
        return totalScore;
    }

    // Setters for scoring fields
    public void setBonusCardPoints(int points) { this.bonusCardPoints = points; }
    public void setEndOfRoundGoalPoints(int points) { this.endOfRoundGoalPoints = points; }
    public void setUnusedFoodTokens(int tokens) { this.unusedFoodTokens = tokens; }
    public void setEggCount(int eggs) { this.eggCount = eggs; }
    public void setCachedFoodCount(int food) { this.cachedFoodCount = food; }
    public void setTuckedCardCount(int tucked) { this.tuckedCardCount = tucked; }

    // Getters for scoring fields
    public int getBirdCardPoints() { return birdCardPoints; }
    public int getBonusCardPoints() { return bonusCardPoints; }
    public int getEndOfRoundGoalPoints() { return endOfRoundGoalPoints; }
    public int getEggCount() { return eggCount; }
    public int getCachedFoodCount() { return cachedFoodCount; }
    public int getTuckedCardCount() { return tuckedCardCount; }
    public int getUnusedFoodTokens() { return unusedFoodTokens; }
    public int getTotalScore() { return totalScore; }

    


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
