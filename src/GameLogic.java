package src;

import java.util.*;

public class GameLogic implements Runnable {
    private final FramePanel panel;
    private final ProgramState state;
    private Game game;
    
    public GameLogic(FramePanel panel, ProgramState state) {
        this.panel = panel;
        this.state = state;
        this.game = new Game(state);
    }
    
    @Override
    public void run() {
    }
    
    public void startGame() {
        game.start();
    }
    
    public boolean performAction(ProgramState.PlayerAction action, Object... params) {
        return game.doAction(action, params);
    }
    
    public void endTurn() {
        game.next();
    }
    
    public void rerollFeeder() {
        game.rerollFeeder();
    }
    
    public void updateCardTray() {
        game.updateCardTray();
    }
    
    public int getCurrentPlayer() {
        return game.getPlayer();
    }
    
    public int getRound() {
        return game.getRound();
    }
    
    public int getActionsRemaining() {
        return game.getActionsRemaining();
    }
    
    public ProgramState.GamePhase getCurrentPhase() {
        return game.getCurrentPhase();
    }
    
    public boolean isGameOver() {
        return game.isOver();
    }
    
    public Feeder getFeeder() {
        return game.getFeeder();
    }
    
    public ArrayList<Bird> getDeck() {
        return state.deckOfCards;
    }
    
    public Bird[] getCardTray() {
        return state.cardTray;
    }
}