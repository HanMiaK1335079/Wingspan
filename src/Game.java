package src;
import java.util.ArrayList;

public class Game {
    private ProgramState state;
    private Feeder feeder;
    private int round = 1;
    private int player = 0;
    private boolean over = false;
    
    public Game(ProgramState s) {
        this.state = s;
        this.feeder = new Feeder(s);
        init();
    }
    
    public void init() {
        for (int i = 0; i < state.players.length; i++) {
            state.players[i] = new Player();
        }
        
        state.currentPhase = ProgramState.GamePhase.SETUP;
    }
    
    public void start() {
        state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
        state.actionsRemaining = 8;
        player = state.firstPlayerToken - 1;
    }
    
    public void next() {
        player = (player + 1) % 4;
        
        if (player == (state.firstPlayerToken - 1)) {
            nextRound();
        }
        
        state.players[player].setPlayerScore(state.players[player].calculateScore());
    }
    
    public void nextRound() {
        round++;
        
        if (round > 4) {
            state.currentPhase = ProgramState.GamePhase.GAME_OVER;
            over = true;
            return;
        }
        
        state.actionsRemaining = 9 - round;
        
        for (int i = 0; i < state.players.length; i++) {
            Player p = state.players[i];
            p.nextRound();
        }
        
        state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
    }
    
    public boolean isOver() {
        return over;
    }
    
    public int getPlayer() {
        return player;
    }
    
    public int getRound() {
        return round;
    }
    
    public boolean doAction(ProgramState.PlayerAction a, Object... params) {
        Player p = state.players[player];
        
        if (p.getActionsRemaining() <= 0) {
            return false;
        }
        
        boolean ok = false;
        
        switch (a) {
            case PLAY_BIRD:
                ok = play(p, params);
                break;
            case GAIN_FOOD:
                ok = food(p, params);
                break;
            case LAY_EGGS:
                ok = eggs(p, params);
                break;
            case DRAW_CARDS:
                ok = draw(p, params);
                break;
        }
        
        if (ok) {
            p.useAction();
        }
        
        return ok;
    }
    
    private boolean play(Player p, Object... params) {
        if (params.length < 2) return false;
        
        if (!(params[0] instanceof Bird) || !(params[1] instanceof String)) {
            return false;
        }
        
        Bird b = (Bird) params[0];
        String h = (String) params[1];
        
        if (!p.getCardsInHand().contains(b)) {
            return false;
        }
        
        if (!b.canAfford(toStrings(p.getFoods()))) {
            return false;
        }
        
        if (!b.canLiveInHabitat(h)) {
            return false;
        }
        
        boolean played = p.playBird(b, h);
        if (played) {
            p.removeCardFromHand(b);
            
            if (b.getActivate().equals("WP")) {
                b.playAbility();
            }
        }
        
        return played;
    }
    
    private ArrayList<String> toStrings(ArrayList<Integer> f) {
        ArrayList<String> s = new ArrayList<>();
        String[] types = {"seed", "fish", "berry", "insect", "rat"};
        
        for (int i = 0; i < f.size() && i < types.length; i++) {
            int count = f.get(i);
            for (int j = 0; j < count; j++) {
                s.add(types[i]);
            }
        }
        
        return s;
    }
    
    public boolean food(Player p, Object... params) {
        String[] types = {"seed", "berry", "insect", "fish", "rat"};
        String f = types[(int)(Math.random() * types.length)];
        p.addFood(f);
        return true;
    }
    
    public boolean eggs(Player p, Object... params) {
        Bird[][] board = p.getPlayerBoard();
        for (int h = 0; h < 3; h++) {
            for (int i = 0; i < board[h].length; i++) {
                if (board[h][i] != null) {
                    if (board[h][i].getStoredEggs() < board[h][i].getMaxEggs()) {
                        board[h][i].addEggs(1);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean draw(Player p, Object... params) {
        if (state.deckOfCards.size() > 0) {
            Bird card = state.deckOfCards.remove(state.deckOfCards.size() - 1);
            p.addCardToHand(card);
            return true;
        }
        return false;
    }
}