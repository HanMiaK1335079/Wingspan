package src;
import java.util.*; 

public class GameLogic implements Runnable {
    private final FramePanel panel;
    private final ProgramState state;
    private Game game;
    private final ArrayList<Bird> birds = new ArrayList<Bird>();
    
    public GameLogic(FramePanel panel, ProgramState state) {
        this.panel = panel;
        this.state = state;
        this.game = new Game(state);
    }
    
    @Override
    public void run() {
        while (!game.isGameOver()) {
            synchronized (state.lock) {
                try {
                    state.lock.wait();
                    processGameState();
                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        determineWinner();
    }
    
    private void processGameState() {
        switch (state.currentPhase) {
            case SETUP:
                handleSetupPhase();
                break;
            case PLAYER_TURN:
                handlePlayerTurn();
                break;
            case END_OF_ROUND:
                handleEndOfRound();
                break;
            case GAME_OVER:
                handleGameOver();
                break;
        }
    }
    
    private void handleSetupPhase() {
        initializeGameComponents();
        state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
        state.CURRENTEVENT.add("PlayerTurn");
    }
    
    private void initializeGameComponents() {
        createBirdDeck();
        dealStartingCards();
    }
    
    private void createBirdDeck() {
        for (int i = 0; i < 10; i++) {
            state.deckOfCards.add(createPlaceholderBird("Bird" + i));
        }
    }
    
    private Bird createPlaceholderBird(String name) {
        ArrayList<String> habitats = new ArrayList<String>();
        habitats.add("f");
        ArrayList<String[]> foodCosts = new ArrayList<String[]>();
        return new Bird(name, "OA", "Placeholder ability", "brown", 1, "cavity", 2, 30, habitats, foodCosts);
    }
    
    private void dealStartingCards() {
        for (int i = 0; i < state.players.length; i++) {
            Player player = state.players[i];
            for (int j = 0; j < 5; j++) {
                if (state.deckOfCards.size() > 0) {
                    Bird card = state.deckOfCards.remove(state.deckOfCards.size() - 1);
                    player.addCardToHand(card);
                }
            }
        }
    }
    
    private void handlePlayerTurn() {
        if (state.CURRENTEVENT.size() > 0) {
            String currentEvent = state.CURRENTEVENT.get(state.CURRENTEVENT.size() - 1);
            if (currentEvent.equals("PlayBird")) {
            } else if (currentEvent.equals("GainFood")) {
            } else if (currentEvent.equals("LayEggs")) {
            } else if (currentEvent.equals("DrawCards")) {
            }
        }
        Player currentPlayer = state.players[game.getCurrentPlayer()];
        if (currentPlayer.getActionsRemaining() <= 0) {
            game.nextPlayer();
            state.CURRENTEVENT.add("NextPlayer");
        }
    }
    
    private void handleEndOfRound() {
        if (game.getCurrentRound() < 4) {
            state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
        } else {
            state.currentPhase = ProgramState.GamePhase.GAME_OVER;
        }
    }
    
    private void handleGameOver() {
        determineWinner();
    }
    
    private void determineWinner() {
        int winningScore = -1;
        int winner = -1;
        for (int i = 0; i < state.players.length; i++) {
            Player player = state.players[i];
            int score = player.calculateScore();
            player.setPlayerScore(score);
            if (score > winningScore) {
                winningScore = score;
                winner = i;
            }
        }
        state.CURRENTEVENT.add("GameOver:Player" + (winner + 1) + " wins with " + winningScore + " points");
    }


}