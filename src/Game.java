package src;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;
import src.Bird;
import src.Food;

public class Game {
    private ProgramState state;
    private Feeder feeder;
    private int round = 1;
    private int player = 0;
    private boolean over = false;
    private ArrayList<Bird> birds;
    public enum GamePhase {
        SETUP,
        INITIAL_SELECTION,
        PLAYER_TURN,
        END_OF_ROUND,
        GAME_OVER
    }
    
    public Game(ProgramState s) {
        this.state = s;
        this.feeder = new Feeder(s);
        init();
    }
    
    public void init() {
        for (int i = 0; i < state.players.length; i++) {
            state.players[i] = new Player("Player " + (i + 1), 0, new ArrayList<>(), new ArrayList<>(), i);
        }
        
        state.makeDeckOfCards();
        
        dealInitialCards();

        dealInitialBonusCards();
        
        setupInitialFood();
        
        setupRoundGoals();
        
        state.currentPhase = ProgramState.GamePhase.INITIAL_SELECTION;
    }
    
    private void setupRoundGoals() {
        
        int[] goalIds = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        
        for (int i = 0; i < goalIds.length; i++) {
            int randomIndex = (int)(Math.random() * goalIds.length);
            int temp = goalIds[i];
            goalIds[i] = goalIds[randomIndex];
            goalIds[randomIndex] = temp;
        }
        
        for (int i = 0; i < 4; i++) {
            state.setRoundGoal(i, goalIds[i]);
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
    
    private void dealInitialBonusCards() {
        for (int i = 0; i < 2; i++) {
            for (int p = 0; p < state.players.length; p++) {
                if (!state.bonusDeck.isEmpty()) {
                    Bonus card = state.bonusDeck.remove(state.bonusDeck.size() - 1);
                    state.players[p].addBonusCard(card);
                }
            }
        }
    }

    private void setupInitialFood() {
        for (int p = 0; p < state.players.length; p++) {
            state.players[p].addFood(Food.FoodType.SEED, 1);
            state.players[p].addFood(Food.FoodType.INSECT, 1);
            state.players[p].addFood(Food.FoodType.FISH, 1);
            state.players[p].addFood(Food.FoodType.BERRY, 1);
            state.players[p].addFood(Food.FoodType.RAT, 1);
        }
    }
    
    public void next() {
        player = (player + 1) % 4;
        
        if (player == (state.firstPlayerToken - 1)) {
            nextRound();
        }
        
        state.players[player].setPlayerScore(state.players[player].calculateScore().total());
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
        
        for (int i = 0; i < state.players.length; i++) {
            Player p = state.players[i];
            p.nextRound();
            p.resetPinkPowers();
        }
        
        state.currentPhase = ProgramState.GamePhase.PLAYER_TURN;
    }
    
    private void determineRoundWinner(int roundNumber) {
        if (roundNumber < 1 || roundNumber > 4) return;
        
        int goalId = state.getRoundGoal(roundNumber - 1);
        List<Integer> winners = new ArrayList<>();
        int maxCount = -1;
        
        for (int i = 0; i < state.players.length; i++) {
            int count = getRoundGoalCount(goalId, state.players[i]);
            if (count > maxCount) {
                maxCount = count;
            }
        }

        for (int i = 0; i < state.players.length; i++) {
            int count = getRoundGoalCount(goalId, state.players[i]);
            if (count == maxCount) {
                winners.add(i);
            }
        }

        int[] points;
        switch (roundNumber) {
            case 1:
                points = new int[]{4, 1, 0};
                break;
            case 2:
                points = new int[]{5, 2, 0};
                break;
            case 3:
                points = new int[]{6, 3, 1};
                break;
            case 4:
                points = new int[]{7, 4, 2};
                break;
            default:
                points = new int[]{0, 0, 0};
                break;
        }

        if (winners.size() == 1) {
            state.players[winners.get(0)].addPoints(points[0]);
        } else if (winners.size() > 1) {
            for (int winner : winners) {
                state.players[winner].addPoints(points[1]);
            }
        }
    }

    private int getRoundGoalCount(int goalId, Player p) {
        switch (goalId) {
            case 0: return p.getBirdsInHabitat("forest").size();
            case 1: return p.getBirdsInHabitat("plains").size();
            case 2: return p.getBirdsInHabitat("wetlands").size();
            case 3: return p.getEggCount();
            case 4: return getCachedFoodCount(p);
            case 5: return getBirdsWithEggsCount(p);
            case 6: return p.getFoodTokens().size();
            case 7: return p.getBoard().getLongestRow();
            case 8: return getMaxBirdsInSingleHabitat(p);
            case 9: return getTuckedCardCount(p);
            case 10: return getDietTypes(p);
            case 11: return p.getBirdsInHabitat("forest").size() + p.getBirdsInHabitat("plains").size();
            case 12: return p.getBirdsInHabitat("forest").size() + p.getBirdsInHabitat("wetlands").size();
            case 13: return p.getBirdsInHabitat("plains").size() + p.getBirdsInHabitat("wetlands").size();
            case 14: return getBirdsWithNoEggsCount(p);
            case 15: return p.getBirdCount();
            default: return 0;
        }
    }

    private int getCachedFoodCount(Player p) {
        int count = 0;
        for (Bird b : p.getAllBirdsOnBoard()) {
            count += b.getFoodCost();
        }
        return count;
    }

    private int getBirdsWithEggsCount(Player p) {
        int count = 0;
        for (Bird b : p.getAllBirdsOnBoard()) {
            if (b.getEggCount() > 0) {
                count++;
            }
        }
        return count;
    }

    private int getMaxBirdsInSingleHabitat(Player p) {
        return Math.max(p.getBirdsInHabitat("forest").size(), Math.max(p.getBirdsInHabitat("plains").size(), p.getBirdsInHabitat("wetlands").size()));
    }

    private int getTuckedCardCount(Player p) {
        int count = 0;
        for (Bird b : p.getAllBirdsOnBoard()) {
            count += b.getTuckedCards();
        }
        return count;
    }

    private int getDietTypes(Player p) {
        Set<Food.FoodType> dietTypes = new HashSet<>();
        for (Bird b : p.getAllBirdsOnBoard()) {
            for (Food.FoodType[] foodOptions : b.getFoods()) {
                for (Food.FoodType food : foodOptions) {
                    dietTypes.add(food);
                }
            }
        }
        return dietTypes.size();
    }

    private int getBirdsWithNoEggsCount(Player p) {
        int count = 0;
        for (Bird b : p.getAllBirdsOnBoard()) {
            if (b.getEggCount() == 0) {
                count++;
            }
        }
        return count;
    }
    
    private void calculateFinalScores() {
        for (int i = 0; i < state.players.length; i++) {
            Player p = state.players[i];
            ScoreBreakdown baseScore = p.calculateScore();
            
            ArrayList<Bonus> bonuses = p.getBonuses();
            int bonusPoints = 0;
            for (Bonus b : bonuses) {
                bonusPoints += b.getBonusPoints(p);
            }
            
            ScoreBreakdown finalScore = new ScoreBreakdown(
                baseScore.printedPoints,
                baseScore.eggs,
                baseScore.cachedFood,
                baseScore.tuckedCards,
                bonusPoints,
                baseScore.flocked
            );
            
            p.setFinalScore(finalScore);
            p.setPlayerScore(finalScore.total());
        }
    }
    
    public boolean isOver() {
        return over;
    }

    public ArrayList<Bird> getBirds() {
        return birds;
    }

    public void setBirds(ArrayList<Bird> birds) {
        this.birds = birds;
    }
    
    public int getPlayer() {
        return player;
    }

    public int getCurrentPlayerIndex() {
        return player;
    }
    
    public int getRound() {
        return round;
    }
    
    public Player getPlayer(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < state.players.length) {
            return state.players[playerIndex];
        }
        return null;
    }
    
    public boolean doAction(ProgramState.PlayerAction action, Object... params) {
        Player p = state.players[player];
        if (p.getActionsRemaining(round) <= 0) return false;

        boolean success = false;
        switch (action) {
            case PLAY_BIRD:
                success = play(p, params);
                break;
            case GAIN_FOOD:
                success = food(p, params);
                break;
            case LAY_EGGS:
                success = eggs(p, params);
                break;
            case DRAW_CARDS:
                String[] drawParams = new String[params.length];
                for (int i = 0; i < params.length; i++) {
                    drawParams[i] = (String) params[i];
                }
                success = draw(p, drawParams);
                break;
        }

        if (success) {
            p.useAction(round);
            triggerPinkPowers(action, p);
        }

        return success;
    }

    private void triggerPinkPowers(ProgramState.PlayerAction action, Object... params) {
        for (int i = 0; i < state.players.length; i++) {
            if (i == player) continue;
            Player other = state.players[i];
            ArrayList<Bird> birds = other.getAllBirdsOnBoard();
            for (Bird b : birds) {
                if (!b.isPinkPowerUsed() && b.triggersOnAction(action)) {
                    handlePinkPower(other, b, action, params);
                    b.setPinkPowerUsed(true);
                }
            }
        }
    }
    
    public boolean play(Player p, Object... params) {
        if (params.length < 2) return false;
        
        if (!(params[0] instanceof Bird) || !(params[1] instanceof String)) {
            return false;
        }
        
        Bird b = (Bird) params[0];
        String h = (String) params[1];
        
        if (!p.getCardsInHand().contains(b)) {
            return false;
        }
        
        if (!p.canAffordBird(b)) {
            return false;
        }
        
        if (!b.canLiveInHabitat(h)) {
            return false;
        }
        
        if (p.isHabitatFull(h)) {
            return false;
        }
        
        Object[] playParams = new Object[params.length - 2];
        if (params.length > 2) {
            System.arraycopy(params, 2, playParams, 0, params.length - 2);
        }
        boolean played = p.playBird(b, h, -1, playParams);
        if (played) {
            p.removeCardFromHand(b);
            
            if (b.isWhenPlayedAbility()) {
                activateWhenPlayedAbility(p, b, params);
            }
        }
        
        return played;
    }

    private boolean food(Player p, Object... params) {
        if (params.length == 0) {
            return false;
        }

        int numToGain = 1 + (p.getBirdsInHabitat("forest") != null ? p.getBirdsInHabitat("forest").size() : 0);
        if (params[0] instanceof ArrayList) {
            ArrayList<Food.FoodType> foodChoices = (ArrayList<Food.FoodType>) params[0];
            if (foodChoices.size() > numToGain) {
                return false;
            }

            for (Food.FoodType food : foodChoices) {
                if (feeder.getDice().contains(food)) {
                    p.addFood(food, 1);
                    feeder.removeDie(feeder.getDice().indexOf(food));
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

        if (feeder.getDice().isEmpty() || feeder.canReroll()) {
            feeder.reRoll();
        }

        return true;
    }

    private boolean eggs(Player p, Object... params) {
        if (params.length == 0) {
            return false;
        }

        int numToLay = 2 + (p.getBirdsInHabitat("plains") != null ? p.getBirdsInHabitat("plains").size() : 0);
        if (params[0] instanceof Map) {
            Map<Bird, Integer> eggsToLay = (Map<Bird, Integer>) params[0];
            int totalEggs = 0;
            for (int eggs : eggsToLay.values()) {
                totalEggs += eggs;
            }

            if (totalEggs > numToLay) {
                return false;
            }

            for (Map.Entry<Bird, Integer> entry : eggsToLay.entrySet()) {
                Bird b = entry.getKey();
                int eggs = entry.getValue();
                if (!p.layEggsOnBird(b, eggs)) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    public void activateWhenPlayedAbility(Player p, Bird b, Object... params) {
        String abilityText = b.getAbilityText();

        if (abilityText.contains("Play a second bird")) {
            String habitat = "";
            if (params.length > 0 && params[0] instanceof String) {
                habitat = (String) params[0];
            } else {
                if (abilityText.contains("[forest]")) {
                    habitat = "forest";
                } else if (abilityText.contains("[grassland]")) {
                    habitat = "plains";
                } else if (abilityText.contains("[wetland]")) {
                    habitat = "wetlands";
                } else if (abilityText.contains("this bird's habitat")) {
                    habitat = p.getHabitatOfBird(b);
                }
            }
        } else if (abilityText.contains("Gain 3 [seed] from the supply.")) {
            p.addFood(Food.FoodType.SEED, 3);
        } else if (abilityText.contains("Gain 3 [fish] from the supply.")) {
            p.addFood(Food.FoodType.FISH, 3);
        } else if (abilityText.contains("Gain all [fish] that are in the birdfeeder.")) {
            int fishGained = feeder.takeAll("fish");
            p.addFood(Food.FoodType.FISH, fishGained);
        } else if (abilityText.contains("Gain all [invertebrate] that are in the birdfeeder.")) {
            int invertebratesGained = feeder.takeAll("insect");
            p.addFood(Food.FoodType.INSECT, invertebratesGained);
        } else if (abilityText.contains("Draw [card] equal to the number of players +1")) {
            int numToDraw = state.players.length + 1;
            ArrayList<Bird> drawnCards = new ArrayList<>();
            for (int i = 0; i < numToDraw && !state.deckOfCards.isEmpty(); i++) {
                drawnCards.add(state.deckOfCards.remove(state.deckOfCards.size() - 1));
            }
            if (!drawnCards.isEmpty()) {
                p.addCardToHand(drawnCards.remove(0));
            }
            for (int i = 0; i < state.players.length - 1; i++) {
                if (!drawnCards.isEmpty()) {
                    state.players[(player + 1 + i) % state.players.length].addCardToHand(drawnCards.remove(0));
                }
            }
        } else if (abilityText.contains("Draw 3 [card] from the deck.")) {
            for (int i = 0; i < 3 && !state.deckOfCards.isEmpty(); i++) {
                p.addCardToHand(state.deckOfCards.remove(state.deckOfCards.size() - 1));
            }
        } else if (abilityText.contains("Lay 1 [egg] on each of your birds with a [cavity] nest.")) {
            p.layEggsInNestType("cavity");
        } else if (abilityText.contains("Lay 1 [egg] on this bird.")) {
            b.addEggs(1);
        } else if (abilityText.contains("Draw 2 new bonus cards and keep 1.")) {
            if (p.canDrawBonusCard()) {
                ArrayList<Bonus> drawnCards = new ArrayList<>();
                for (int i = 0; i < 2 && !state.bonusDeck.isEmpty(); i++) {
                    drawnCards.add(state.bonusDeck.remove(state.bonusDeck.size() - 1));
                }
                if (params.length > 0 && params[0] instanceof Bonus) {
                    Bonus cardToKeep = (Bonus) params[0];
                    if (drawnCards.contains(cardToKeep)) {
                        p.addBonusCard(cardToKeep);
                        drawnCards.remove(cardToKeep);
                    }
                    state.bonusDeck.addAll(drawnCards);
                } else if (!drawnCards.isEmpty()) {
                    p.addBonusCard(drawnCards.remove(0));
                    state.bonusDeck.addAll(drawnCards);
                }
            }
        }
    }

    private void activateBrownAbility(Bird b, Player p) {
        String abilityText = b.getAbilityText();
        if (b.isPredatorAbility()) {
            if (b.isRollDieAbility()) {
                // Roll die for each player and check for success
                boolean success = false;
                for (int i = 0; i < state.players.length; i++) {
                    if (feeder.takeRandomFood() != null) {
                        success = true;
                        break;
                    }
                }
                if (success) {
                    p.addFood(Food.FoodType.INSECT, 1);
                }
            }
        }
    }

    private void handlePinkPower(Player p, Bird b, ProgramState.PlayerAction action, Object... params) {
        String abilityText = b.getAbilityText();
        if (action == ProgramState.PlayerAction.LAY_EGGS) {
            if (abilityText.contains("When another player takes the \"lay eggs\" action")) {
                if (abilityText.contains("[ground] nest")) {
                    p.layEggsInNestType("ground");
                } else if (abilityText.contains("[cavity] nest")) {
                    p.layEggsInNestType("cavity");
                } else if (abilityText.contains("[bowl] nest")) {
                    p.layEggsInNestType("bowl");
                }
            }
        } else if (action == ProgramState.PlayerAction.PLAY_BIRD) {
            if (abilityText.contains("When another player plays a bird in their [wetland]")) {
                p.addFood(Food.FoodType.FISH, 1);
            } else if (abilityText.contains("When another player plays a bird in their [forest]")) {
                p.addFood(Food.FoodType.INSECT, 1);
            } else if (abilityText.contains("When another player plays a bird in their [grassland]")) {
                if (p.getCardsInHand().size() > 0) {
                    Bird cardToTuck = p.getCardsInHand().get(0);
                    p.removeCardFromHand(cardToTuck);
                    b.tuckCard();
                }
            }
        } else if (action == ProgramState.PlayerAction.PREDATOR_SUCCESS) {
            if (abilityText.contains("When another player's [predator] succeeds")) {
                p.addFood(feeder.takeRandomFood(), 1);
            }
        } else if (action == ProgramState.PlayerAction.GAIN_FOOD) {
            if (abilityText.contains("if they gain any number of [rodent]")) {
                if (params.length > 0 && params[0] instanceof ArrayList) {
                    ArrayList<String> foodGained = (ArrayList<String>) params[0];
                    if (foodGained.contains("rodent")) {
                        b.cacheFood();
                    }
                }
            }
        }
    }

    private void giveAllPlayersFood(Food.FoodType foodType, int amount) {
        for (Player player : state.players) {
            player.addFood(foodType, amount);
        }
    }

    private void giveAllPlayersCard() {
        for (Player player : state.players) {
            if (!state.deckOfCards.isEmpty()) {
                player.addCardToHand(state.deckOfCards.remove(state.deckOfCards.size() - 1));
            }
        }
    }

    private boolean draw(Player p, String[] params) {
        int numToDraw = 1 + p.getBirdsInHabitat("wetlands").size();
        if (p.getActionsRemaining(getRound()) > 0) {
            if (params[0].equals("0")) { 
                for (int i = 0; i < numToDraw; i++) {
                    p.addCardToHand(state.deckOfCards.remove(state.deckOfCards.size() - 1));
                }
                p.useAction(getRound());
                return true;
            } else { 
                int index = Integer.parseInt(params[1]);
                if (state.cardTray[index] != null) {
                    p.addCardToHand(state.cardTray[index]);
                    state.cardTray[index] = state.deckOfCards.remove(state.deckOfCards.size() - 1);
                    p.useAction(getRound());
                    return true;
                }
            }
        }
        return false;
    }

    public void rerollFeeder() {
        feeder.reRoll();
    }

    public void updateCardTray() {
        for (int i = 0; i < state.cardTray.length; i++) {
            if (state.cardTray[i] == null) {
                if (!state.deckOfCards.isEmpty()) {
                    state.cardTray[i] = state.deckOfCards.remove(state.deckOfCards.size() - 1);
                }
            }
        }
    }

    public int getActionsRemaining() {
        return state.players[player].getActionsRemaining(round);
    }

    public ProgramState.GamePhase getCurrentPhase() {
        return state.currentPhase;
    }

    public Feeder getFeeder() {
        return feeder;
    }

    public Player[] getPlayers() {
        return state.players;
    }
}