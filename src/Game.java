package src;

public class Game {
    private ProgramState state;
    private Feeder feeder;
    private int currentRound = 1;
    private int currentPlayer = 0;
    private boolean gameOver = false;
    
    public Game(ProgramState state) {
        this.state = state;
        this.feeder = new Feeder(state);
        initializeGame();
    }
    
    private void initializeGame() {
        for (int i = 0; i < state.players.length; i++) {
        }
        
        state.currentPhase = ProgramState.GamePhase.SETUP;
    }
    
    public void startGame() {
        state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
        state.actionsRemaining = 8;
        currentPlayer = state.firstPlayerToken - 1;
    }
    
    public void nextPlayer() {
        currentPlayer = (currentPlayer + 1) % 4;
        
        if (currentPlayer == (state.firstPlayerToken - 1)) {
            nextRound();
        }
        
        state.players[currentPlayer].setPlayerScore(state.players[currentPlayer].calculateScore());
    }
    
    private void nextRound() {
        currentRound++;
        
        if (currentRound > 4) {
            state.currentPhase = ProgramState.GamePhase.GAME_OVER;
            gameOver = true;
            return;
        }
        
        state.actionsRemaining = 9 - currentRound;
        
        for (int i = 0; i < state.players.length; i++) {
            Player player = state.players[i];
            player.nextRound();
        }
        
        state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    public int getCurrentRound() {
        return currentRound;
    }
    
    public boolean executePlayerAction(ProgramState.PlayerAction action, Object... params) {
        Player player = state.players[currentPlayer];
        
        if (player.getActionsRemaining() <= 0) {
            return false;
        }
        
        boolean success = false;
        
        switch (action) {
            case PLAY_BIRD:
                success = playBirdAction(player, params);
                break;
            case GAIN_FOOD:
                success = gainFoodAction(player, params);
                break;
            case LAY_EGGS:
                success = layEggsAction(player, params);
                break;
            case DRAW_CARDS:
                success = drawCardsAction(player, params);
                break;
        }
        
        if (success) {
            player.useAction();
        }
        
        return success;
    }
    
    private boolean layEggsAction(Player player, Object[] params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'layEggsAction'");
    }

    private boolean playBirdAction(Player player, Object... params) {
        if (params.length < 2) return false;
        
        if (!(params[0] instanceof Bird) || !(params[1] instanceof String)) {
            return false;
        }
        
        Bird bird = (Bird) params[0];
        String habitat = (String) params[1];
        
        if (!player.getCards().contains(bird)) {
            return false;
        }
        
        if (!bird.canAfford(player.getFoods())) {
            return false;
        }
        
        if (!bird.canLiveInHabitat(habitat)) {
            return false;
        }
        
        boolean played = player.playBird(bird, habitat);
        if (played) {
            player.removeCard(bird);
            
            if (bird.getActivate().equals("WP")) {
                bird.playAbility();
            }
        }
        
        return played;
    }
    
    private boolean gainFoodAction(Player player, Object... params) {
        String[] foodTypes = {"Seed", "Fruit", "Invertebrate", "Fish", "Rodent"};
        String food = foodTypes[(int)(Math.random() * foodTypes.length)];
        player.addFood(food);
        return true;
    }
    
    
    private boolean drawCardsAction(Player player, Object... params) {
        if (state.deckOfCards.size() > 0) {
            Bird card = state.deckOfCards.remove(state.deckOfCards.size() - 1);
            player.addCard(card);
            return true;
        }
        return false;
    }
}