package src;
import java.util.*;

public class ProgramState {
    public final Object lock = new Object();
    public volatile int round=0;
    public volatile int playerTurn=0;
    public volatile ArrayList<Bird> deckOfCards = new ArrayList<>();
    public volatile ArrayList<Bird> discardPile = new ArrayList<>();
    public volatile Bird[] cardTray = new Bird[3];
    public volatile ArrayList<Bird> birds = new ArrayList<>();

    public volatile Player[] players = new Player[4];
    public volatile int playing = 0;
    public volatile int showing = 0;

    public volatile int birdNumArrayForChoosingSpecificBird=0;

    public volatile boolean canPressInfoButton=true;//PLEASE MAKE THIS FALSE DURING ANIMATIONS.
    public volatile int firstPlayerToken=((int)(Math.random()*4))+1;
    
    public ArrayList<String> CURRENTEVENT = new ArrayList<>();


    public volatile boolean[][] squaresClickedToPlayBird = new boolean[3][5];
    private int[] roundGoals = new int[4];
    private int[] roundWinners = new int[4];

    public void resetSquaresClickedToPlayBird() {
        for (int i = 0; i < squaresClickedToPlayBird.length; i++) {
            for (int j = 0; j < squaresClickedToPlayBird[i].length; j++) {
                squaresClickedToPlayBird[i][j] = false;
            }
        }
    }

    public void makeDeckOfCards(){
        deckOfCards.addAll(birds);
        Collections.shuffle(deckOfCards);
    }
        
    
    public enum GamePhase {
        SETUP,
        PLAYER_TURN,
        END_OF_ROUND,
        GAME_OVER
    }
    
    public enum PlayerAction {
        PLAY_BIRD,
        GAIN_FOOD,
        LAY_EGGS,
        DRAW_CARDS
    }
    
    public enum DetailedAction {
        PLAY_BIRD_TO_FOREST,
        PLAY_BIRD_TO_PLAINS,
        PLAY_BIRD_TO_WETLANDS,
        GAIN_FOOD_FROM_SUPPLY,
        GAIN_FOOD_FROM_FEEDER,
        LAY_EGG_ON_BIRD,
        DRAW_CARDS_FROM_DECK,
        REROLL_FEEDER,
        CACHE_FOOD_ON_BIRD
    }
    
    public volatile GamePhase currentPhase = GamePhase.SETUP;
    public volatile int actionsRemaining = 8;
    public volatile String habitatToPlayBird = "";
    
    public volatile int[][] playerActionCounts = new int[4][4]; 
    public volatile int[] playerRoundScores = new int[4]; 
    public volatile boolean[] playerHasBonusCard = new boolean[4]; 
    
    public ProgramState(){
      
    }


    
    public void incrementPlayerActionCount(int playerIndex, int actionType) {
        if (playerIndex >= 0 && playerIndex < 4 && actionType >= 0 && actionType < 4) {
            playerActionCounts[playerIndex][actionType]++;
        }
    }
    
    public int getPlayerActionCount(int playerIndex, int actionType) {
        if (playerIndex >= 0 && playerIndex < 4 && actionType >= 0 && actionType < 4) {
            return playerActionCounts[playerIndex][actionType];
        }
        return 0;
    }
    
    public void setPlayerRoundScore(int playerIndex, int round, int score) {
        if (playerIndex >= 0 && playerIndex < 4) {
            playerRoundScores[playerIndex] = score;
        }
    }
    
    public int getPlayerRoundScore(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < 4) {
            return playerRoundScores[playerIndex];
        }
        return 0;
    }
    
    public void setPlayerHasBonusCard(int playerIndex, boolean hasBonus) {
        if (playerIndex >= 0 && playerIndex < 4) {
            playerHasBonusCard[playerIndex] = hasBonus;
        }
    }
    
    public boolean getPlayerHasBonusCard(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < 4) {
            return playerHasBonusCard[playerIndex];
        }
        return false;
    }
    
    public void setRoundGoal(int roundIndex, int goalId) {
        if (roundIndex >= 0 && roundIndex < 4) {
            roundGoals[roundIndex] = goalId;
        }
    }
    
    public int getRoundGoal(int roundIndex) {
        if (roundIndex >= 0 && roundIndex < 4) {
            return roundGoals[roundIndex];
        }
        return 0;
    }
    
    public void setRoundWinner(int roundIndex, int playerIndex) {
        if (roundIndex >= 0 && roundIndex < 4 && playerIndex >= 0 && playerIndex < 4) {
            roundWinners[roundIndex] = playerIndex;
        }
    }
    
    public int getRoundWinner(int roundIndex) {
        if (roundIndex >= 0 && roundIndex < 4) {
            return roundWinners[roundIndex];
        }
        return 0;
    }
}