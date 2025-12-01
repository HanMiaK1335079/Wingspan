package src;

import java.util.*;
import src.Bird;
import src.Player;
import src.Bonus;
import src.Food;

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
    
    public Game getGame() {
        return game;
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
    
    public List<Bird> getBirds() {
        return game.getBirds();
    }

    public void setBirds(ArrayList<Bird> birds) {
        game.setBirds(birds);
    }

    public Player getCurrentPlayer() {
        return game.getPlayer(game.getCurrentPlayerIndex());
    }
    
    public int getRound() {
        return game.getRound();
    }
    
    public int getActionsRemaining() {
        return game.getActionsRemaining();
    }

    public ArrayList<Bird> getCurrentPlayerHand() {
        return game.getPlayer(game.getCurrentPlayerIndex()).getCardsInHand();
    }

    public ArrayList<Bonus> getCurrentPlayerBonuses() {
        return game.getPlayer(game.getCurrentPlayerIndex()).getBonuses();
    }

    public int getFoodCount(Food.FoodType type) {
        return game.getPlayer(game.getCurrentPlayerIndex()).getFoodCount(type);
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