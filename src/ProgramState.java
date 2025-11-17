package src;
import java.util.*;

public class ProgramState {
    public final Object lock = new Object();
    public volatile int round=0;
    public volatile int playerTurn=0;
    public volatile ArrayList<Bird> deckOfCards = new ArrayList<>();
    public volatile ArrayList<Bird> discardPile = new ArrayList<>();
    public volatile Bird[] cardTray = new Bird[3];
    
    public volatile Player[] players = new Player[4];
    public volatile int playing = 0;

    public volatile boolean canPressInfoButton=true;//PLEASE MAKE THIS FALSE DURING ANIMATIONS.
    public volatile int firstPlayerToken=((int)(Math.random()*4))+1;
    
    public ArrayList<String> CURRENTEVENT = new ArrayList<>();
    
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
    
    public volatile GamePhase currentPhase = GamePhase.SETUP;
    public volatile int actionsRemaining = 8;
    
    public ProgramState(){
      
    }


}
