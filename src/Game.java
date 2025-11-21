package src;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
        
        state.makeDeckOfCards();
        
        dealInitialCards();
        
        setupInitialFood();
        
        setupRoundGoals();
        
        state.currentPhase = ProgramState.GamePhase.SETUP;
    }
    
    private void setupRoundGoals() {
        for (int i = 0; i < 4; i++) {
            int goalId = (int)(Math.random() * 16);
            state.setRoundGoal(i, goalId);
        }
    }
    
    private void dealInitialCards() {
        for (int i = 0; i < 5; i++) {
            for (int p = 0; p < state.players.length; p++) {
                if (!state.deckOfCards.isEmpty()) {
                    Bird card = state.deckOfCards.remove(state.deckOfCards.size() - 1);
                    state.players[p].addCardToHand(card);
                }
            }
        }
    }
    
    private void setupInitialFood() {
        for (int p = 0; p < state.players.length; p++) {
            state.players[p].addFood("seed");
            state.players[p].addFood("insect");
        }
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
        determineRoundWinner(round);
        
        round++;
        
        if (round > 4) {
            state.currentPhase = ProgramState.GamePhase.GAME_OVER;
            over = true;
            calculateFinalScores();
            return;
        }
        
        state.actionsRemaining = 9 - round;
        
        for (int i = 0; i < state.players.length; i++) {
            Player p = state.players[i];
            p.nextRound();
        }
        
        state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
    }
    
    private void determineRoundWinner(int roundNumber) {
        // TODO: handle ties
        // TODO: not handled for all player counts, ranking, tie based scoring
        if (roundNumber < 1 || roundNumber > 4) return;
        
        int goalId = state.getRoundGoal(roundNumber - 1);
        int winner = 0;
        int maxCount = -1;
        
        switch (goalId) {
            case 0:
                for (int i = 0; i < state.players.length; i++) {
                    int count = state.players[i].getBirdsInHabitat("forest").size();
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < state.players.length; i++) {
                    int count = state.players[i].getBirdsInHabitat("plains").size();
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < state.players.length; i++) {
                    int count = state.players[i].getBirdsInHabitat("wetlands").size();
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < state.players.length; i++) {
                    int count = state.players[i].getEggCount();
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 4:
                for (int i = 0; i < state.players.length; i++) {
                    int count = getCachedFoodCount(state.players[i]);
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 5:
                for (int i = 0; i < state.players.length; i++) {
                    int count = getBirdsWithEggsCount(state.players[i]);
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 6:
                for (int i = 0; i < state.players.length; i++) {
                    int count = state.players[i].getFoodTokens().size();
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 7:
                for (int i = 0; i < state.players.length; i++) {
                    int count = state.players[i].getBoard().getLongestRow();
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 8:
                for (int i = 0; i < state.players.length; i++) {
                    int count = getMaxBirdsInSingleHabitat(state.players[i]);
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 9:
                for (int i = 0; i < state.players.length; i++) {
                    int count = getTuckedCardCount(state.players[i]);
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 10:
                for (int i = 0; i < state.players.length; i++) {
                    int count = getDietTypes(state.players[i]);
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 11:
                for (int i = 0; i < state.players.length; i++) {
                    int forest = state.players[i].getBirdsInHabitat("forest").size();
                    int grassland = state.players[i].getBirdsInHabitat("plains").size();
                    int count = forest + grassland;
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 12:
                for (int i = 0; i < state.players.length; i++) {
                    int forest = state.players[i].getBirdsInHabitat("forest").size();
                    int wetland = state.players[i].getBirdsInHabitat("wetlands").size();
                    int count = forest + wetland;
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 13:
                for (int i = 0; i < state.players.length; i++) {
                    int grassland = state.players[i].getBirdsInHabitat("plains").size();
                    int wetland = state.players[i].getBirdsInHabitat("wetlands").size();
                    int count = grassland + wetland;
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 14:
                for (int i = 0; i < state.players.length; i++) {
                    int count = getBirdsWithNoEggsCount(state.players[i]);
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
            case 15:
                for (int i = 0; i < state.players.length; i++) {
                    int count = state.players[i].getBirdCount();
                    if (count > maxCount) {
                        maxCount = count;
                        winner = i;
                    }
                }
                break;
        }
        
        state.setRoundWinner(roundNumber - 1, winner);
    }
    
    private int getCachedFoodCount(Player p) {
        int count = 0;
        ArrayList<Bird> birds = p.getAllBirdsOnBoard();
        for (Bird b : birds) {
            count += b.getCachedFood();
        }
        return count;
    }
    
    private int getBirdsWithEggsCount(Player p) {
        int count = 0;
        ArrayList<Bird> birds = p.getAllBirdsOnBoard();
        for (Bird b : birds) {
            if (b.getStoredEggs() > 0) {
                count++;
            }
        }
        return count;
    }
    
    private int getTuckedCardCount(Player p) {
        int count = 0;
        ArrayList<Bird> birds = p.getAllBirdsOnBoard();
        for (Bird b : birds) {
            count += b.getTuckedCards().size();
        }
        return count;
    }
    
    private int getDietTypes(Player p) {
        Set<String> foodTypes = new HashSet<>();
        ArrayList<String> foods = p.getFoodTokens();
        
        for (String food : foods) {
            foodTypes.add(food);
        }
        
        return foodTypes.size();
    }
    
    private int getMaxBirdsInSingleHabitat(Player p) {
        int maxCount = 0;
        
        for (int h = 0; h < 3; h++) {
            String habitat = "";
            switch (h) {
                case 0: habitat = "forest"; break;
                case 1: habitat = "plains"; break;
                case 2: habitat = "wetlands"; break;
            }
            
            int count = p.getBirdsInHabitat(habitat).size();
            if (count > maxCount) {
                maxCount = count;
            }
        }
        
        return maxCount;
    }
    
    private int getBirdsWithNoEggsCount(Player p) {
        int count = 0;
        ArrayList<Bird> birds = p.getAllBirdsOnBoard();
        for (Bird b : birds) {
            if (b.getStoredEggs() == 0) {
                count++;
            }
        }
        return count;
    }
    
    private void calculateFinalScores() {
        for (int i = 0; i < state.players.length; i++) {
            Player p = state.players[i];
            int baseScore = p.calculateScore();
            
            ArrayList<Bonus> bonuses = p.getBonuses();
            int bonusPoints = 0;
            for (Bonus b : bonuses) {
                bonusPoints += b.getBonusPoints(p);
            }
            
            p.setPlayerScore(baseScore + bonusPoints);
        }
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
                // TODO: Implement bird ability activation
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
    
    public int getActionsRemaining() {
        return state.actionsRemaining;
    }
    
    public ProgramState.GamePhase getCurrentPhase() {
        return state.currentPhase;
    }
    
    public Feeder getFeeder() {
        return feeder;
    }
    
    public void rerollFeeder() {
        if (feeder.canReroll()) {
            feeder.reRoll();
        }
    }
    
    public void updateCardTray() {
        for (int i = 0; i < 3; i++) {
            if (state.cardTray[i] == null && !state.deckOfCards.isEmpty()) {
                state.cardTray[i] = state.deckOfCards.remove(state.deckOfCards.size() - 1);
            }
        }
    }
    
    public int getRoundGoal(int roundIndex) {
        return state.getRoundGoal(roundIndex);
    }
    
    public int getRoundWinner(int roundIndex) {
        return state.getRoundWinner(roundIndex);
    }
}