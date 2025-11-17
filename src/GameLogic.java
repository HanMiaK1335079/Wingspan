package src;

import java.util.*;


import java.util.*; 

public class GameLogic implements Runnable {
    private final FramePanel panel;
    private final ProgramState state;
     private final ArrayList<Bird> birds = new ArrayList<>();
    private Game game;
    private final ArrayList<Bird> birds = new ArrayList<Bird>();
    
    public GameLogic(FramePanel panel, ProgramState state) {
        this.panel = panel;
        this.state = state;
        this.game = new Game(state);
    }
    
    @Override
    public void run() {
        
       
    }
     

}
