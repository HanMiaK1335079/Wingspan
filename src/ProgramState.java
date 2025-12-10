package src;
import java.util.*;

public class ProgramState {
    public final Object lock = new Object();
    public volatile int round=0;
    public volatile int playerTurn=0;
    public volatile ArrayList<Bird> deckOfCards = new ArrayList<>();
    public volatile ArrayList<Bird> discardPile = new ArrayList<>();
    public volatile ArrayList<Bonus> bonusDeck = new ArrayList<>();
    public volatile ArrayList<Bonus> discardedBonusCards = new ArrayList<>();
    public volatile Bird[] cardTray = new Bird[3];
    public volatile ArrayList<Bird> birds = new ArrayList<>();

    public volatile Player[] players = new Player[4];
    public volatile int playing = 0;
    public volatile int showing = 0;

    public volatile boolean hasCheckedForNumberOfRemovableDice=false;
    public volatile int numberOfRemovableDice=0;
    public volatile boolean canTradeCardForMoreFood=false;

    public volatile int birdNumArrayForChoosingSpecificBird=0;

    public volatile Bird specificBirdToPlay=null;
    public volatile int[] birdFoodsForPlayingBird=new int[5];

    public volatile int eggsNeededToSpendForPlayingBird=0;

    public volatile boolean canPressInfoButton=true;//PLEASE MAKE THIS FALSE DURING ANIMATIONS.
    public volatile int firstPlayerToken=((int)(Math.random()*4))+1;

        public ArrayList<String> CURRENTEVENT = new ArrayList<>();

        

        
    public void nextTurn(){
        playing=(playing+1)%4;
        
    }
    public enum GameEvent {
        GAME_START,
        SELECT_SCREEN,
        GAME,
        VIEW_BIRDS,
        VIEW_BONUS,
        VIEW_DRAW_BIRDS,
        VIEW_FEEDER,
        INFO,
        RULES,
        DRAW_CARDS,
        PLAY_BIRD,
        GAIN_FOOD,
        LAY_EGGS,
        PROCESS_MOUSE_CLICK_GAME_START,
        SCORE_ROUND
    }

    public volatile GameEvent currentEvent = GameEvent.GAME_START;

    public GameEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(GameEvent event) {
        this.currentEvent = event;
    }

    public volatile boolean[][] squaresClickedToPlayBird = new boolean[3][5];
    private final int[] roundGoals = new int[4];
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

    public ArrayList<Bird> getDeck() {
        return deckOfCards;
    }

    public ArrayList<Bonus> getBonuses() {
        return bonusDeck;
    }
        
    
    public enum GamePhase {
        SETUP,
        INITIAL_SELECTION,
        PLAYER_TURN,
        END_OF_ROUND,
        GAME_OVER
    }
    
    public enum PlayerAction {
        PLAY_BIRD,
        GAIN_FOOD,
        LAY_EGGS,
        DRAW_CARDS,
        PREDATOR_SUCCESS,
        ACTIVATE_BROWN_POWER
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
    public Game game = new Game(this);
}