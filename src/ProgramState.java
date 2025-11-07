package src;
import java.util.*;

public class ProgramState {
        public final Object lock = new Object();
    public volatile int round=0;
    public volatile int playerTurn=0;
    public volatile ArrayList<Bird> deckOfCards = new ArrayList<>();
    public volatile ArrayList<Bird> discardPile = new ArrayList<>();
    public volatile Bird[] cardTray = new Bird[3];

    public volatile Player playerOne= new Player();
    public volatile Player playerTwo= new Player();
    public volatile Player playerThree= new Player();
    public volatile Player playerFour= new Player();
    public volatile boolean canPressInfoButton=true;//PLEASE MAKE THIS FALSE DURING ANIMATIONS.
    public volatile int firstPlayerToken=((int)(Math.random()*4))+1;

    public ArrayList<String> CURRENTEVENT = new ArrayList<>();
    
    public ProgramState(){
        
    }


}
