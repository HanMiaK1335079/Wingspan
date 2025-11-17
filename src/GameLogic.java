package src;

import java.io.File;
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
        File birdCSV = new File("src/birdInfo.csv");
        readCSV(birdCSV);
    }
     public void readCSV(File f){//MIA MADE THIS IT'S HER PROBLEM NOW
        try {
            //out.println("Will activate scanner");
            Scanner scan = new Scanner(f);
            //out.println("Read the scanner");
            Bird b;
            String[] items;
            while (scan.hasNextLine()){
                String l = scan.nextLine();
                //out.println("line: " + l);
                if (l.contains("\"")){
                    l = l.replace("\"\"", "");
                    String[] quoteSplit = l.split("\"");
                    //out.println("quotesplit: "+Arrays.toString(quoteSplit));
                    ArrayList<String> supportSplit = new ArrayList<String>();
                    supportSplit.addAll(Arrays.asList(quoteSplit[0].split(",")));
                    supportSplit.add(quoteSplit[1]);
                    supportSplit.addAll(Arrays.asList(quoteSplit[2].split(",")));
                    supportSplit.remove(3);
                    items = new String[supportSplit.size()];
                    //out.println("Support: "+supportSplit);
                    for (int i=0;i<items.length;i++) items[i] = supportSplit.get(i);
                    
                    //for (int i=0;i<items.length;i++) out.print(i+": "+items[i]+"        ");
                }else{
                    items = l.split(",");
                }


                // Doing all the food parsing yey
                Map<Integer, String> foodMap = new HashMap<Integer, String>();
                String[] foodtypes = {"i", "s", "f", "b", "r", "","a"};
                for (int i=13;i<20;i++) foodMap.put(i, foodtypes[i-13]);
                ArrayList<String[]> foodArr = new ArrayList<String[]>();
                ArrayList<String> foods = new ArrayList<String>();
                //out.println("Food stuff instantiated");
                if (items[20].equals("/")){
                    //out.println("Activated splitfoods");
                    for (int i=13;i<18;i++)
                        if (!items[i].equals("")) foods.add(foodMap.get(i));

                    for (String fo: foods){
                        String[] foo = {fo};
                        foodArr.add(foo);
                    }
                    
                }else{
                    //out.println("Activated setfoods");
                    ArrayList<String> foo = new ArrayList<String>();
                    for (int i=13;i<20;i++){
                        if (!items[i].equals("")){
                            //out.println("Adding");
                            for (int j=0;j<Integer.parseInt(items[i]);j++){
                                foo.add(foodMap.get(i));
                                //out.println("Added food to foodMap");
                            }
                        }
                    }
                    String[] foox = new String[foo.size()];
                    for (int i=0;i<foo.size();i++) foox[i] = foo.get(i);
                    //out.println(Arrays.toString(foox));
                    foodArr.add(foox);
                }

                // Ability type stuff
                //out.println("Got to abilityType stuff");
                String abilityType;
                if (items[3].equals("X")) abilityType = "predator";
                else if (items[4].equals("X")) abilityType = "flocking";
                else if (items[5].equals("X")) abilityType = "bonus";
                else abilityType = "";

                //ablityActivate stuff
                //out.println("Got to abilityActivate stuff");
                String abilityActivate;
                abilityActivate = switch (items[1]) {
                    case "brown" -> "OA";
                    case "white" -> "WP";
                    case "pink" -> "OBT";
                    default -> "N";
                };

                //habitat stuff
                //out.println("Got to Habitat stuff");
                ArrayList<String> habitats = new ArrayList<String>();
                if (items[10].equals("X")) habitats.add("f");
                if (items[11].equals("X")) habitats.add("p");
                if (items[12].equals("X")) habitats.add("w");
                //out.println("Items: " + items[11]+items[12]+items[13]);
                //out.println("Habitats: "+habitats);

                //out.println("Got to birdmaking stuff");
                
                b = new Bird(items[0], abilityActivate, items[2], abilityType, Integer.parseInt(items[6]), items[7], Integer.parseInt(items[8]), Integer.parseInt(items[9]), habitats, foodArr);
                birds.add(b);
                //out.println("Finished birdmaking stuff");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e + "\ncsv reading ran into issue");
        }
        
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